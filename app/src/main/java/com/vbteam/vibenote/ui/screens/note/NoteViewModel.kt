package com.vbteam.vibenote.ui.screens.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbteam.vibenote.data.auth.AuthManager
import com.vbteam.vibenote.data.repository.NotesRepository
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.data.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    fun loadNote(noteId: String?) {
        if (noteId == null) {
            _uiState.value = NoteUiState()
            return
        }

        updateLoadingState(LoadingState.Loading)
        
        viewModelScope.launch {
            try {
                val note = notesRepository.getNoteById(noteId)
                if (note != null) {
                    updateNoteData(note)
                    checkNoteSyncStatus(noteId)
                    
                    // Проверяем наличие анализа в облаке
                    if (!note.isAnalyzed) {
                        try {
                            notesRepository.checkNoteAnalysis(noteId)?.let { analysis ->
                                // Получаем обновленную заметку с анализом
                                notesRepository.getNoteById(noteId)?.let { updatedNote ->
                                    updateNoteData(updatedNote)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("NoteViewModel", "Failed to check note analysis", e)
                        }
                    }
                } else {
                    showMessage(UiMessage.NotFound)
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Failed to load note", e)
                showMessage(UiMessage.LoadError)
            } finally {
                updateLoadingState(LoadingState.Idle)
            }
        }
    }

    fun updateContent(newContent: String) {
        _uiState.update { currentState ->
            currentState.copy(
                content = newContent,
                title = newContent.take(30),
                updatedAt = LocalDateTime.now(),
                syncState = SyncState.NotSynced
            )
        }
    }

    fun saveNote(saveToCloud: Boolean = false) {
        val state = uiState.value
        if (state.content.isBlank()) return

        viewModelScope.launch {
            try {
                // Для локального сохранения
                if (!saveToCloud) {
                    // Если заметка синхронизирована, не сохраняем
                    if (state.syncState == SyncState.Synced) return@launch
                    
                    // Если у заметки есть ID и контент не изменился, не сохраняем
                    if (state.id != null) {
                        val existingNote = notesRepository.getNoteById(state.id)
                        if (existingNote?.content == state.content) return@launch
                    }
                }

                // Создаем заметку с правильным ID
                val note = if (state.id != null) {
                    // Если у заметки уже есть ID, обновляем существующую
                    Note(
                        id = state.id,
                        cloudId = state.cloudId,
                        content = state.content,
                        createdAt = state.createdAt ?: LocalDateTime.now(),
                        updatedAt = LocalDateTime.now(),
                        tags = state.tags,
                        analysis = state.analysis,
                        isSyncedWithCloud = saveToCloud
                    )
                } else {
                    // Если это новая заметка, создаем с новым ID
                    Note.create(state.content)
                }
                
                // Для сохранения в облако
                if (saveToCloud && !isUserAuthenticated()) {
                    showMessage(UiMessage.AuthRequired)
                    return@launch
                }

                notesRepository.saveNote(note, saveToCloud)
                
                // После сохранения обновляем UI state с актуальными данными
                val savedNote = notesRepository.getNoteById(note.id)
                savedNote?.let { updateNoteData(it) }
                
                if (saveToCloud) {
                    checkNoteSyncStatus(note.id)
                }
                
                showMessage(UiMessage.SaveSuccess)
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Failed to save note", e)
                showMessage(UiMessage.SaveError)
            }
        }
    }

    fun analyzeNote() {
        val state = uiState.value
        if (state.syncState != SyncState.Synced) {
            showMessage(UiMessage.NotSynced)
            return
        }

        if (state.id == null) {
            showMessage(UiMessage.SaveError)
            return
        }

        if (state.cloudId == null) {
            showMessage(UiMessage.NotSynced)
            return
        }

        viewModelScope.launch {
            try {
                updateLoadingState(LoadingState.Analyzing)
                val analysis = notesRepository.requestNoteAnalysis(state.id)
                if (analysis != null) {
                    // Получаем обновленную заметку из репозитория
                    val updatedNote = notesRepository.getNoteById(state.id)
                    updatedNote?.let { updateNoteData(it) }
                    
                    showMessage(UiMessage.AnalysisSuccess)
                } else {
                    showMessage(UiMessage.AnalysisError)
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Failed to analyze note", e)
                when (e) {
                    is IllegalStateException -> showMessage(UiMessage.NotSynced)
                    else -> showMessage(UiMessage.AnalysisError)
                }
            } finally {
                updateLoadingState(LoadingState.Idle)
            }
        }
    }

    // UI Message handlers
    fun showMessage(message: UiMessage) {
        _uiState.update { it.copy(uiMessage = message) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(uiMessage = null) }
    }

    // Private helper methods
    private fun isUserAuthenticated(): Boolean =
        authManager.currentUser.value != null

    private suspend fun checkNoteSyncStatus(noteId: String) {
        updateLoadingState(LoadingState.CheckingSync)
        try {
            notesRepository.updateNoteSyncStatus(noteId)
            val updatedNote = notesRepository.getNoteById(noteId)
            updatedNote?.let { note ->
                _uiState.update {
                    it.copy(
                        cloudId = note.cloudId,
                        syncState = if (note.isSyncedWithCloud) SyncState.Synced else SyncState.NotSynced
                    )
                }
            }
        } finally {
            updateLoadingState(LoadingState.Idle)
        }
    }

    private fun updateLoadingState(state: LoadingState) {
        _uiState.update { it.copy(loadingState = state) }
    }

    private fun updateNoteData(note: Note) {
        _uiState.update {
            it.copy(
                id = note.id,
                cloudId = note.cloudId,
                content = note.content,
                title = note.content.take(30),
                tags = note.tags,
                syncState = if (note.isSyncedWithCloud) SyncState.Synced else SyncState.NotSynced,
                isAnalyzed = note.isAnalyzed,
                analysis = note.analysis,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt
            )
        }
        
        if (note.analysis != null) {
            Log.d("NoteViewModel", "Note has analysis: ${note.analysis}")
            Log.d("NoteViewModel", "Note has tags: ${note.tags}")
        }
    }

    private fun createNoteFromState(state: NoteUiState): Note {
        return Note(
            id = state.id ?: UUID.randomUUID().toString(),
            cloudId = state.cloudId,
            content = state.content,
            createdAt = state.createdAt ?: LocalDateTime.now(),
            updatedAt = state.updatedAt ?: LocalDateTime.now(),
            tags = state.tags,
            analysis = state.analysis,
            isSyncedWithCloud = state.syncState == SyncState.Synced
        )
    }
}

