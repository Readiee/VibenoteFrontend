package com.vbteam.vibenote.ui.screens.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotesRoute(
    navController: NavHostController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    val uiState by viewModel.uiState.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    NotesScreenUI(
        navController = navController,
        uiState = uiState,
        onFilterSelected = { viewModel.onFilterSelected(it) },
        onNoteClick = { note ->
            if (uiState.isSearching) {
                viewModel.onNoteClickedFromSearch(note)
            }
            navController.navigate("note/${note.id}")
        },
        onNoteSelected = { viewModel.toggleNoteSelection(it) },
        onAddNoteClick = {
            navController.navigate("note")
        },
        onClearSelection = { viewModel.clearSelection() },
        onDeleteSelected = {
            viewModel.deleteSelectedNotes()
        },
        onSearchClose = { viewModel.onSearchClose() },
        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
        onSearchClicked = { viewModel.onSearchClicked() },
        onSyncWithCloud = { viewModel.syncWithCloud() },
        searchHistory = searchHistory,
        clearSearchHistory = { viewModel.clearSearchHistory() }
    )
}