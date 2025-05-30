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

    NoteScreenUI(
        navController = navController,
        viewModel = viewModel
    )
}

