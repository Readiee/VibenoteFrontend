package com.vbteam.vibenote.ui.screens.note.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vbteam.vibenote.ui.screens.note.NoteUiState

@Composable
fun NoteTextTab(
    uiState: NoteUiState,
    onNoteChanged: (String) -> Unit
) {
    TextField(
        value = uiState.text,
        onValueChange = onNoteChanged,
        placeholder = { Text("Текст", style = MaterialTheme.typography.bodyMedium) },
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
        maxLines = Int.MAX_VALUE,
        singleLine = false,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
