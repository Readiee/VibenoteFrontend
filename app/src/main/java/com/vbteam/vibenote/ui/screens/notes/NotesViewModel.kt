package com.vbteam.vibenote.ui.screens.notes

import androidx.compose.runtime.mutableStateListOf
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
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private var notesJob: Job? = null

    init {
        loadNotes()
    }

    fun loadNotes() {
        _uiState.update { it.copy(isLoading = true) }
        notesJob?.cancel()
        notesJob = viewModelScope.launch {
            notesRepository.getAllNotes().collect { notes ->
                _uiState.update {
                    it.copy(notes = notes, isLoading = false)
                }
            }
        }
    }

    fun onFilterSelected(filter: Emotion) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepository.addNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)

            _searchHistory.update { history ->
                history.filter { it.id != note.id }
            }
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

    // история поиска
    private val _searchHistory = MutableStateFlow<List<Note>>(emptyList())
    val searchHistory: StateFlow<List<Note>> = _searchHistory

    fun onNoteClickedFromSearch(note: Note) {
        if (_searchHistory.value.none { it.id == note.id }) {
            _searchHistory.update { listOf(note) + it.take(19) } // максимум 20
        }
    }

    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }

}