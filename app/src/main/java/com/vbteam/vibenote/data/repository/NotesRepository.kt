package com.vbteam.vibenote.data.repository

import com.vbteam.vibenote.data.local.LocalNoteDao
import com.vbteam.vibenote.data.mapper.toDomain
import com.vbteam.vibenote.data.mapper.toEntity
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.data.remote.CloudService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val localNoteDao: LocalNoteDao,
    private val cloudService: CloudService
) {

    fun getAllNotes(): Flow<List<Note>> =
        localNoteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun addNote(note: Note) {
        localNoteDao.insertNote(note.toEntity())
    }

    suspend fun deleteNote(note: Note) {
        localNoteDao.deleteNote(note.toEntity())
    }

    suspend fun getNoteById(noteId: String): Note? {
        return localNoteDao.getNoteById(noteId)?.toDomain()
    }

    suspend fun saveNote(note: Note, saveToCloud: Boolean = false) {
        localNoteDao.insertNote(note.toEntity()) // Локальное сохранение

        if (saveToCloud) {
            cloudService.saveNoteToCloud(note) // Сохранение в облако
        }
    }

}
