package com.vbteam.vibenote.ui.screens.notes

import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.Note

data class NotesUiState(
    val selectedFilter: Emotion = Emotion.ALL,
    val notes: List<Note> = emptyList<Note>(),
    val selectedNotes: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,

    val isSearching: Boolean = false,
    val searchQuery: String = "",

    // Error states
    val showLoadError: Boolean = false,
    val showSyncError: Boolean = false
)
