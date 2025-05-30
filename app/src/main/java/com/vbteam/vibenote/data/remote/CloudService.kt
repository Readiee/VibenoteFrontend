package com.vbteam.vibenote.data.remote

import android.util.Log
import com.vbteam.vibenote.data.mapper.toDomain
import com.vbteam.vibenote.data.mapper.toCreateRequest
import com.vbteam.vibenote.data.mapper.toUpdateRequest
import com.vbteam.vibenote.data.mapper.DateConverter
import com.vbteam.vibenote.data.model.Analysis
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.data.remote.api.*
import com.vbteam.vibenote.data.remote.api.model.EntryResponse
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudService @Inject constructor(
    private val authApi: AuthApi,
    private val entryApi: EntryApi
) {
    suspend fun saveNoteToCloud(note: Note): Note? {
        return handleApiCall("save note") {
            val request = note.toCreateRequest()
            val response = entryApi.createEntry(request)
            mapToNote(response, note.id)
        }
    }

    suspend fun getNoteFromCloud(noteId: String): Note? {
        return handleApiCall("get note") {
            val response = entryApi.getEntry(noteId)
            mapToNote(response, noteId)
        }
    }

    suspend fun analyzeNote(noteId: String): Analysis? {
        return handleApiCall("analyze note") {
            val response = entryApi.analyzeEntry(noteId)
            Analysis(
                id = response.id,
                entryText = response.entryText,
                result = response.result,
                tags = response.tags.map { it.toDomain() }
            )
        }
    }

    suspend fun syncNotes(): List<Note> {
        return handleApiCall("sync notes", emptyList()) {
            entryApi.getEntries().map { it.toDomain() }
        } ?: emptyList()
    }

    suspend fun updateNoteInCloud(note: Note): Note? {
        if (note.cloudId == null) {
            Log.e("CloudService", "Cannot update note without cloudId")
            return null
        }

        return handleApiCall("update note") {
            val request = note.toUpdateRequest()
            val response = entryApi.updateEntry(note.cloudId, request)
            mapToNote(response, note.id)
        }
    }

    private fun mapToNote(response: EntryResponse, localId: String?): Note {
        return Note(
            id = localId ?: response.id,
            cloudId = response.id,
            content = response.content,
            createdAt = DateConverter.parseDateTime(response.createdAt),
            updatedAt = DateConverter.parseDateTime(response.updatedAt),
            analysis = response.analysis?.toDomain(),
            isSyncedWithCloud = true
        )
    }

    private suspend fun <T> handleApiCall(
        operation: String,
        defaultValue: T? = null,
        block: suspend () -> T
    ): T? {
        return try {
            block()
        } catch (e: HttpException) {
            logHttpError(operation, e)
            defaultValue
        } catch (e: SocketTimeoutException) {
            Log.e("CloudService", "Timeout while $operation", e)
            defaultValue
        } catch (e: IOException) {
            Log.e("CloudService", "IO error while $operation", e)
            defaultValue
        } catch (e: Exception) {
            Log.e("CloudService", "Failed to $operation", e)
            defaultValue
        }
    }

    private fun logHttpError(operation: String, e: HttpException) {
        Log.e("CloudService", "HTTP error while $operation: ${e.code()}", e)
        when (e.code()) {
            401 -> Log.e("CloudService", "Unauthorized: Invalid token")
            403 -> Log.e("CloudService", "Forbidden: Not enough access rights")
            404 -> Log.e("CloudService", "Resource not found")
            500 -> Log.e("CloudService", "Server error")
            else -> Log.e("CloudService", "Network error")
        }
    }
}