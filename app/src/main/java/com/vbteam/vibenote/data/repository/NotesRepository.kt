package com.vbteam.vibenote.data.repository

import android.util.Log
import com.vbteam.vibenote.data.local.LocalNoteDao
import com.vbteam.vibenote.data.mapper.toDomain
import com.vbteam.vibenote.data.mapper.toEntity
import com.vbteam.vibenote.data.model.Analysis
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.data.model.Tag
import com.vbteam.vibenote.data.remote.CloudService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val localNoteDao: LocalNoteDao,
    private val cloudService: CloudService
) {
    private val noteCache = mutableMapOf<String, Note>()

    fun getAllNotes(): Flow<List<Note>> =
        localNoteDao.getAllNotes().map { entities ->
            Log.d("NotesRepository", "Mapping ${entities.size} note entities to domain models")
            entities.map { it.toDomain() }
        }

    suspend fun getNoteById(noteId: String): Note? {
        noteCache[noteId]?.let { return it }

        val localNote = localNoteDao.getNoteById(noteId)?.toDomain()
        if (localNote != null) {
            noteCache[noteId] = localNote
            // Запускаем обновление статуса синхронизации в фоновом режиме, не дожидаясь его
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val updatedNote = updateSyncStatusIfNeeded(localNote)
                    if (updatedNote != localNote) {
                        saveLocally(updatedNote)
                    }
                } catch (e: Exception) {
                    Log.e("NotesRepository", "Failed to update sync status in background", e)
                }
            }
        }

        return localNote
    }

    suspend fun createNoteLocally(content: String): Note {
        val note = Note.create(content)
        saveLocally(note)
        return note
    }

    suspend fun saveNote(note: Note, saveToCloud: Boolean = false) {
        when {
            // сохраняем в облако
            saveToCloud -> {
                val cloudNote = saveToCloud(note)
                if (cloudNote != null) {
                    saveLocally(cloudNote)
                } else {
                    throw Exception("Failed to save note to cloud")
                }
            }
            // Если локальное сохранение заметки, которая уже была в облаке
            note.cloudId != null -> {
                val unsyncedNote = note.copy(isSyncedWithCloud = false)
                saveLocally(unsyncedNote)
            }
            // локальное сохранение
            else -> saveLocally(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        try {
            // Удаляем локально
            localNoteDao.deleteNote(note.toEntity())
            noteCache.remove(note.id)
            Log.d("NotesRepository", "Note deleted successfully: ${note.id}")

            // Если есть cloudId, удаляем из облака
            if (note.cloudId != null) {
                try {
                    cloudService.deleteNoteFromCloud(note.cloudId)
                } catch (e: Exception) {
                    Log.e("NotesRepository", "Failed to delete note from cloud", e)
                    throw e
                }
            }

        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to delete note", e)
            throw e
        }
    }

    suspend fun checkNoteAnalysis(noteId: String): Analysis? {
        val note = getNoteById(noteId) ?: throw IllegalArgumentException("Note not found")

        if (!note.isSyncedWithCloud) {
            throw IllegalStateException("Cannot check analysis for note that is not synced with cloud")
        }

        val cloudId = note.cloudId ?: throw IllegalStateException("Note has no cloud ID")

        // Получаем существующий анализ из облака
        try {
            val cloudNote = cloudService.getNoteFromCloud(cloudId)
            if (cloudNote?.analysis != null) {
                Log.d("NotesRepository", "Found existing analysis for note $cloudId")
                val updatedNote = note.copy(
                    analysis = cloudNote.analysis,
                    tags = cloudNote.analysis.tags.map { analysisTag ->
                        Tag(
                            name = analysisTag.tag.name,
                            value = analysisTag.value
                        )
                    }
                )
                saveLocally(updatedNote)
                return cloudNote.analysis
            }
        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to get note from cloud", e)
        }

        return null
    }

    suspend fun requestNoteAnalysis(noteId: String): Analysis? {
        val note = getNoteById(noteId) ?: throw IllegalArgumentException("Note not found")

        if (!note.isSyncedWithCloud) {
            throw IllegalStateException("Cannot analyze note that is not synced with cloud")
        }

        val cloudId = note.cloudId ?: throw IllegalStateException("Note has no cloud ID")

        Log.d("NotesRepository", "Requesting new analysis for note $cloudId")
        return cloudService.analyzeNote(cloudId)?.also { analysis ->
            val updatedNote = note.copy(
                analysis = analysis,
                tags = analysis.tags.map { analysisTag ->
                    Tag(
                        name = analysisTag.tag.name,
                        value = analysisTag.value
                    )
                }
            )
            saveLocally(updatedNote)
        }
    }

    suspend fun syncWithCloud() {
        try {
            val cloudNotes = cloudService.syncNotes()
            val localNotes = localNoteDao.getAllNotesAsList().map { it.toDomain() }

            // Обработка облачных заметок: загрузка в локальное хранилище
            processCloudNotes(cloudNotes, localNotes)
        } catch (e: Exception) {
            throw Exception("Failed to sync with cloud", e)
        }
    }

    suspend fun updateNoteSyncStatus(noteId: String) {
        val localNote = getNoteById(noteId) ?: return
        val updatedNote = updateSyncStatusIfNeeded(localNote)
        if (updatedNote != localNote) {
            saveLocally(updatedNote)
        }
    }

    suspend fun loadMissingNotesFromCloud() {
        try {
            Log.d("NotesRepository", "Starting to load missing notes from cloud")
            val cloudNotes = cloudService.syncNotes()
            Log.d("NotesRepository", "Received ${cloudNotes.size} notes from cloud")

            val localNotes = localNoteDao.getAllNotesAsList().map { it.toDomain() }
            Log.d("NotesRepository", "Found ${localNotes.size} local notes")

            cloudNotes.forEach { cloudNote ->
                val existingLocalNote = localNotes.find {
                    it.cloudId == cloudNote.cloudId || (it.cloudId == null && it.content == cloudNote.content)
                }

                if (existingLocalNote == null) {
                    // Note does not exist locally, save it
                    Log.d("NotesRepository", "Saving missing note with cloudId: ${cloudNote.cloudId ?: "N/A"}")
                    saveLocally(cloudNote.copy(isSyncedWithCloud = true))
                } else if (existingLocalNote.updatedAt < cloudNote.updatedAt) {
                    // Cloud note is newer, update local note
                    Log.d("NotesRepository", "Updating existing local note with cloudId: ${cloudNote.cloudId ?: "N/A"}")
                    val updatedNote = existingLocalNote.copy(
                        cloudId = cloudNote.cloudId,
                        content = cloudNote.content,
                        createdAt = cloudNote.createdAt,
                        updatedAt = cloudNote.updatedAt,
                        analysis = cloudNote.analysis,
                        tags = cloudNote.tags,
                        isSyncedWithCloud = true
                    )
                    saveLocally(updatedNote)
                } else if (existingLocalNote.cloudId == null && cloudNote.cloudId != null) {
                    // Local note exists but not synced, update with cloudId
                    Log.d("NotesRepository", "Updating existing local note with cloudId from cloud: ${cloudNote.cloudId}")
                    val updatedNote = existingLocalNote.copy(
                        cloudId = cloudNote.cloudId,
                        isSyncedWithCloud = true
                    )
                    saveLocally(updatedNote)
                }
            }

            Log.d("NotesRepository", "Finished loading missing notes from cloud")
        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to load missing notes from cloud", e)
            throw Exception("Failed to load missing notes from cloud", e)
        }
    }

    // Вспомогательные методы

    private suspend fun saveLocally(note: Note) {
        Log.d("NotesRepository", "Saving note locally: ${note.id}")
        localNoteDao.insertNote(note.toEntity())
        noteCache[note.id] = note
        Log.d("NotesRepository", "Note saved successfully: ${note.id}")
    }

    private suspend fun saveToCloud(note: Note): Note? {
        return try {
            if (note.cloudId != null) {
                cloudService.updateNoteInCloud(note)
            } else {
                cloudService.saveNoteToCloud(note)
            }?.copy(isSyncedWithCloud = true)
        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to save note to cloud", e)
            null
        }
    }

    private suspend fun updateSyncStatusIfNeeded(note: Note): Note {
        if (note.cloudId == null) return note

        return try {
            val cloudNote = cloudService.getNoteFromCloud(note.cloudId)
            if (cloudNote != null) {
                note.copy(
                    cloudId = cloudNote.cloudId,
                    isSyncedWithCloud = note.content.trim() == cloudNote.content.trim()
                )
            } else {
                note.copy(isSyncedWithCloud = false)
            }
        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to check sync status", e)
            note.copy(isSyncedWithCloud = false)
        }
    }

    private suspend fun processCloudNotes(cloudNotes: List<Note>, localNotes: List<Note>) {
        for (cloudNote in cloudNotes) {
            val existingLocalNote = localNotes.find { localNote ->
                localNote.cloudId == cloudNote.cloudId ||
                (localNote.cloudId == null && localNote.content.trim() == cloudNote.content.trim())
            }

            when {
                existingLocalNote == null -> {
                    // Note does not exist locally, save it
                    saveLocally(cloudNote.copy(isSyncedWithCloud = true))
                }
                cloudNote.updatedAt > existingLocalNote.updatedAt -> {
                    // Cloud note is newer, update local note
                    val updatedNote = existingLocalNote.copy(
                        content = cloudNote.content,
                        updatedAt = cloudNote.updatedAt,
                        cloudId = cloudNote.cloudId,
                        isSyncedWithCloud = true,
                        analysis = cloudNote.analysis,
                        tags = cloudNote.tags
                    )
                    saveLocally(updatedNote)
                }
                existingLocalNote.cloudId == null && cloudNote.cloudId != null -> {
                    // Local note exists but not synced, update with cloudId
                    val updatedNote = existingLocalNote.copy(
                        cloudId = cloudNote.cloudId,
                        isSyncedWithCloud = true
                    )
                    saveLocally(updatedNote)
                }
            }
        }
    }
}
