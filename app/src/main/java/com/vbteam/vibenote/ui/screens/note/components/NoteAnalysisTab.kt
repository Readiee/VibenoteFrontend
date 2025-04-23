package com.vbteam.vibenote.ui.screens.note.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.ui.screens.note.NoteUiState
import androidx.compose.ui.res.painterResource
import com.vbteam.vibenote.R
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.vbteam.vibenote.ui.components.AppButtonType
import com.vbteam.vibenote.ui.components.BaseButton

@Composable
fun NoteAnalysisTab(uiState: NoteUiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.illustration_cross),
            contentDescription = null,
            modifier = Modifier
                .width(128.dp)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Текст не готов к анализу",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Сохраните запись в облако во вкладке «Запись», прежде чем его проанализировать.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        BaseButton(
            onClick = { /* handle click */ },
            modifier = Modifier
                .fillMaxWidth(),
            text = "Анализировать запись",
            type = AppButtonType.PRIMARY,
            enabled = uiState.isSyncedWithCloud
        )
    }
}
