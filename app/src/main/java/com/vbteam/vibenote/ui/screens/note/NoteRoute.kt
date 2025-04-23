package com.vbteam.vibenote.ui.screens.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteRoute(
    navController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel(),
    noteId: String? = null
) {
    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn = false // пока тут и пока false

    NoteScreen(
        navController = navController,
        uiState = uiState,
        onBack = {
            viewModel.saveNote()
            navController.popBackStack()
        },
        onSaveButtonClicked = {
            if (isLoggedIn) {
                viewModel.saveNote(saveToCloud = true)
            } else {
                viewModel.showAuthRequiredDialog() // какой-то треш 2
            }
        },
        onNoteChanged = { viewModel.updateText(it) }, // какой-то треш 1
        onDismissAuthDialog = { viewModel.dismissAuthRequiredDialog() },
        onConfirmationAuthDialog = {
            viewModel.saveNote()
            viewModel.dismissAuthRequiredDialog()
            navController.navigate("sign_in")
        }
    )
}

