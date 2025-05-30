package com.vbteam.vibenote.ui.screens.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbteam.vibenote.data.repository.NotesRepository
import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _searchHistoryIds = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<Note>> = combine(
        _searchHistoryIds,
        _uiState
    ) { ids, state ->
        ids.mapNotNull { id -> state.notes.find { it.id == id } }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private var notesJob: Job? = null

    init {
        Log.d("NotesViewModel", "Initializing NotesViewModel")
        loadNotes()
    }

    fun loadNotes() {
        Log.d("NotesViewModel", "Starting to load notes")
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                Log.d("NotesViewModel", "Loading local notes...")
                
                // Запускаем сбор данных из Flow в отдельной корутине
                viewModelScope.launch {
                    notesRepository.getAllNotes()
                        .collect { notes ->
                            Log.d("NotesViewModel", "Received ${notes.size} notes from local DB")
                            _uiState.update { state ->
                                state.copy(
                                    notes = notes,
                                    isLoading = false
                                )
                            }
                        }
                }
                
                // Загружаем недостающие заметки из облака
                try {
                    Log.d("NotesViewModel", "Loading missing notes from cloud...")
                    notesRepository.loadMissingNotesFromCloud()
                    Log.d("NotesViewModel", "Successfully loaded missing notes from cloud")
                } catch (e: Exception) {
                    Log.e("NotesViewModel", "Failed to load missing notes", e)
                }
                
                // Синхронизируем с облаком (только загрузка)
                Log.d("NotesViewModel", "Starting cloud sync...")
                syncWithCloud()
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Failed to load notes", e)
                _uiState.update { it.copy(
                    isLoading = false,
                    showLoadError = true
                ) }
            }
        }
    }

    fun syncWithCloud() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSyncing = true) }
                notesRepository.syncWithCloud()
                _uiState.update { it.copy(showSyncError = false) }
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Failed to sync with cloud", e)
                _uiState.update { it.copy(showSyncError = true) }
            } finally {
                _uiState.update { it.copy(isSyncing = false) }
            }
        }
    }

    fun onFilterSelected(filter: Emotion) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepository.createNoteLocally(note.content)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    fun toggleNoteSelection(noteId: String) {
        val currentSelection = _uiState.value.selectedNotes
        val wasEmpty = currentSelection.isEmpty()
        _uiState.update { state ->
            state.copy(
                selectedNotes = if (currentSelection.contains(noteId)) {
                    currentSelection - noteId
                } else {
                    currentSelection + noteId
                }
            )
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedNotes = emptySet()) }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            val selectedNotes = _uiState.value.selectedNotes
            val notesToDelete = _uiState.value.notes.filter { it.id in selectedNotes }
            notesToDelete.forEach { deleteNote(it) }
            clearSelection()
        }
    }

    fun onSearchClicked() {
        _uiState.update { it.copy(isSearching = true) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSearchClose() {
        _uiState.update { it.copy(isSearching = false, searchQuery = "") }
    }

    fun onNoteClickedFromSearch(note: Note) {
        _searchHistoryIds.update { current ->
            if (note.id in current) current
            else listOf(note.id) + current.take(19) // максимум 20
        }
    }

    fun clearSearchHistory() {
        _searchHistoryIds.value = emptyList()
    }

    fun dismissLoadError() {
        _uiState.update { it.copy(showLoadError = false) }
    }

    fun dismissSyncError() {
        _uiState.update { it.copy(showSyncError = false) }
    }
}