package com.vbteam.vibenote.ui.screens.note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vbteam.vibenote.ui.screens.note.NoteUiState

@Composable
fun NoteTextTab(
    uiState: NoteUiState,
    onNoteChanged: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 2.dp)
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(0.dp))
            TextField(
                value = uiState.text,
                onValueChange = onNoteChanged,
                placeholder = { Text("Текст", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal
                ),
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
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
