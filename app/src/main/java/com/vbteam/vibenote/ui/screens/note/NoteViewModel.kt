package com.vbteam.vibenote.ui.screens.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbteam.vibenote.data.auth.AuthManager
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.data.repository.NotesRepository
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
                // Сначала пытаемся загрузить локальную сразу
                val localNote = notesRepository.getNoteById(noteId)
                if (localNote != null) {
                    // Update UI with local data immediately
                    updateNoteData(localNote)
                    updateLoadingState(LoadingState.Idle) // Set idle after local data is loaded
                } else {
                    showMessage(UiMessage.NotFound)
                    updateLoadingState(LoadingState.Idle)
                    return@launch
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Failed to load note locally", e)
                showMessage(UiMessage.LoadError)
                updateLoadingState(LoadingState.Idle)
                return@launch
            }
        }

        // Затем синхронизируем при необходимости в отдельной корутине, не блокируя UI
        viewModelScope.launch {
            try {
                checkNoteSyncStatus(noteId)
                val updatedNote = notesRepository.getNoteById(noteId)
                updatedNote?.let { note ->
                    if (!note.isAnalyzed) {
                        notesRepository.checkNoteAnalysis(note.id)?.let {
                            notesRepository.getNoteById(note.id)?.let { latestNote ->
                                updateNoteData(latestNote)
                            }
                        }
                    } else {
                        updateNoteData(note)
                    }
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Failed to sync with cloud or check analysis", e)
            }
        }
    }

    fun updateContent(newContent: String) {
        _uiState.update { currentState ->
            currentState.copy(
                content = newContent,
                title = newContent.take(30),
                updatedAt = LocalDateTime.now(),
                hasLocalChanges = true,
                syncState = if (currentState.cloudId != null)
                    SyncState.UnsyncedChanges
                else
                    SyncState.NotSynced
            )
        }
    }

    fun saveNote(saveToCloud: Boolean = false) {
        val state = uiState.value
        if (state.content.isBlank()) return

        viewModelScope.launch {
            if (saveToCloud) {
                _uiState.update { it.copy(
                    isSavingToCloud = true,
                    syncState = SyncState.SyncInProgress
                ) }
            }

            try {
                // Для локального сохранения
                if (!saveToCloud) {
                    // Если заметка синхронизирована, помечаем как несинхронизированную
                    if (state.syncState == SyncState.Synced) {
                        _uiState.update { it.copy(syncState = SyncState.UnsyncedChanges) }
                    }

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
                        content = state.content.trim(),
                        createdAt = state.createdAt,
                        updatedAt = LocalDateTime.now(),
                        tags = state.tags,
                        analysis = state.analysis,
                        isSyncedWithCloud = false // Всегда false до успешного сохранения в облако
                    )
                } else {
                    // Если это новая заметка, создаем с новым ID
                    Note.create(state.content.trim())
                }

                // Для сохранения в облако
                if (saveToCloud && !isUserAuthenticated()) {
                    showMessage(UiMessage.AuthRequired)
                    return@launch
                }

                // Сначала сохраняем локально
                if (!saveToCloud) {
                    notesRepository.saveNote(note, false)
                    val savedNote = notesRepository.getNoteById(note.id)
                    savedNote?.let { updateNoteData(it) }
                    _uiState.update { it.copy(hasLocalChanges = false) }
                    return@launch
                }

                // Пытаемся сохранить в облако
                try {
                    notesRepository.saveNote(note, true)
                    val savedNote = notesRepository.getNoteById(note.id)
                    savedNote?.let { updateNoteData(it) }
                    checkNoteSyncStatus(note.id)
                    _uiState.update { it.copy(
                        hasLocalChanges = false,
                        syncState = SyncState.Synced
                    ) }
                    showMessage(UiMessage.SaveSuccess)
                } catch (e: Exception) {
                    Log.e("NoteViewModel", "Failed to save note to cloud", e)
                    // Сохраняем локально в случае ошибки облака
                    notesRepository.saveNote(note, false)
                    _uiState.update { it.copy(
                        hasLocalChanges = false,
                        syncState = if (note.cloudId != null) SyncState.UnsyncedChanges else SyncState.NotSynced
                    ) }
                    showMessage(UiMessage.SaveError)
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Failed to save note", e)
                showMessage(UiMessage.SaveError)
                _uiState.update { it.copy(
                    syncState = if (state.cloudId != null) SyncState.UnsyncedChanges else SyncState.NotSynced
                ) }
            } finally {
                if (saveToCloud) {
                    _uiState.update { it.copy(isSavingToCloud = false) }
                }
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
                        syncState = when {
                            note.isAnalyzed -> SyncState.Analyzed
                            note.isSyncedWithCloud -> SyncState.Synced
                            note.cloudId != null -> SyncState.UnsyncedChanges
                            else -> SyncState.NotSynced
                        }
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
                syncState = when {
                    note.isAnalyzed -> SyncState.Analyzed
                    note.isSyncedWithCloud -> SyncState.Synced
                    note.cloudId != null -> SyncState.UnsyncedChanges
                    else -> SyncState.NotSynced
                },
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
            createdAt = state.createdAt,
            updatedAt = state.updatedAt,
            tags = state.tags,
            analysis = state.analysis,
            isSyncedWithCloud = state.syncState == SyncState.Synced
        )
    }

    // Сохранение заметки при выходе (doesn't work yet)
    override fun onCleared() {
        super.onCleared()
        if (uiState.value.hasLocalChanges) {
            saveNote(false)
        }
    }
}

