package com.vbteam.vibenote.data.remote

import android.util.Log
import com.vbteam.vibenote.data.model.Note
import javax.inject.Inject

class CloudService @Inject constructor() {
    suspend fun saveNoteToCloud(note: Note) {
        try {
            val noteDTO = ServerNoteDTO(
                entryId = note.id,
                content = note.text,
                createdAt = note.createdAt.toString(),
                updatedAt = note.updatedAt.toString(),
                emotionTagId = note.emotionTagId,
                emotionName = note.emotionName,
                emotionValue = note.emotionValue
            )

//            firestore.collection("notes")
//                .document(note.id)
//                .set(noteDTO)
//                .await()

            Log.d("CloudService", noteDTO.toString() + " saved*")

        } catch (e: Exception) {
            throw Exception("Failed to save note to cloud", e)
        }
    }

//    suspend fun getNoteFromCloud(noteId: String): Note? {
//    }
}