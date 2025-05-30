package com.vbteam.vibenote.data.repository

import android.util.Log
import com.vbteam.vibenote.data.local.LocalNoteDao
import com.vbteam.vibenote.data.mapper.toDomain
import com.vbteam.vibenote.data.mapper.toEntity
import com.vbteam.vibenote.data.model.Analysis
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.data.model.Tag
import com.vbteam.vibenote.data.remote.CloudService
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
        if (localNote == null) return null

        val updatedNote = updateSyncStatusIfNeeded(localNote)
        noteCache[noteId] = updatedNote
        return updatedNote
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
                cloudNote?.let { saveLocally(it) }
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
            // Если есть cloudId, сначала удаляем из облака
            if (note.cloudId != null) {
                try {
                    cloudService.deleteNoteFromCloud(note.cloudId)
                } catch (e: Exception) {
                    Log.e("NotesRepository", "Failed to delete note from cloud", e)
                    throw e
                }
            }

            // Затем удаляем локально
            localNoteDao.deleteNote(note.toEntity())
            noteCache.remove(note.id)
            Log.d("NotesRepository", "Note deleted successfully: ${note.id}")
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

            // Находим отсутствующие локально заметки (по cloudId)
            val localCloudIds = localNotes.mapNotNull { it.cloudId }.toSet()
            val missingNotes = cloudNotes.filter { cloudNote ->
                cloudNote.cloudId != null && !localCloudIds.contains(cloudNote.cloudId)
            }
            Log.d("NotesRepository", "Found ${missingNotes.size} missing notes to download")

            missingNotes.forEach { note ->
                Log.d("NotesRepository", "Saving missing note with cloudId: ${note.cloudId}")
                saveLocally(note.copy(isSyncedWithCloud = true))
            }
            Log.d("NotesRepository", "Successfully saved all missing notes")
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
                    isSyncedWithCloud = note.content == cloudNote.content
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
            val localNote = localNotes.find { it.cloudId == cloudNote.cloudId || it.id == cloudNote.id }
            
            when {
                localNote == null -> saveLocally(cloudNote)
                cloudNote.updatedAt > localNote.updatedAt -> {
                    val updatedNote = localNote.copy(
                        content = cloudNote.content,
                        updatedAt = cloudNote.updatedAt,
                        cloudId = cloudNote.cloudId,
                        isSyncedWithCloud = true,
                        analysis = cloudNote.analysis,
                        tags = cloudNote.tags
                    )
                    saveLocally(updatedNote)
                }
            }
        }
    }
}
