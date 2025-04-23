package com.vbteam.vibenote.ui.screens.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.R

@Composable
fun EmptyNotesPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(horizontal = 32.dp)
            .offset(y = 200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_empty_notes),
            contentDescription = null,
            modifier = Modifier
                .width(140.dp)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.empty_notes_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.empty_notes_description),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
