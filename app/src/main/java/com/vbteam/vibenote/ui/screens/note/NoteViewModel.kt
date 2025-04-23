package com.vbteam.vibenote.ui.screens.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbteam.vibenote.data.repository.NotesRepository
import com.vbteam.vibenote.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    fun loadNote(noteId: String?) {
        if (noteId == null) {
            _uiState.value = NoteUiState()
        } else {
            viewModelScope.launch {
                val note = notesRepository.getNoteById(noteId)
                if (note != null) {
                    _uiState.value = NoteUiState(
                        id = note.id,
                        title = note.title,
                        text = note.text,
                        emotion = note.emotion,
                        isSyncedWithCloud = note.isSyncedWithCloud,
                        createdAt = note.createdAt,
                        updatedAt = note.updatedAt,
                    )
                } else {
                    Log.w("NoteViewModel", "Note with id $noteId not found")
                }
            }
        }
    }

    fun updateText(newText: String) {
        _uiState.value = _uiState.value.copy(text = newText, updatedAt = LocalDateTime.now())
    }

    fun saveNote(saveToCloud: Boolean = false) {
        val state = uiState.value
        val hasContent = state.text.isNotBlank()

        if (hasContent) {
            val note = Note(
                id = state.id ?: UUID.randomUUID().toString(),
                title = state.text.take(30),
                text = state.text.trim(),
                emotion = state.emotion,
                createdAt = state.createdAt,
                updatedAt = state.updatedAt ?: LocalDateTime.now(),
                emotionName = state.emotion.name,
                emotionTagId = "",
                emotionValue = 0,
                isSyncedWithCloud = saveToCloud
            )

            if (saveToCloud && state.text.length <= 100) {
                Log.w("NoteViewModel", "Text is too short to save to cloud")
                return
            }

            viewModelScope.launch {
                notesRepository.saveNote(note, saveToCloud)
            }
        }
    }

    fun showAuthRequiredDialog() {
        _uiState.update { it.copy(showAuthRequiredDialog = true) }
    }
    fun dismissAuthRequiredDialog() {
        _uiState.update { it.copy(showAuthRequiredDialog = false) }
    }
}

