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

}