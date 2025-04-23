package com.vbteam.vibenote.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun NullAlignTopBar() {
    IconButton(onClick = {}) {
        Icon(
            imageVector = Icons.Outlined.ChevronLeft,
            contentDescription = "null_align",
            tint = MaterialTheme.colorScheme.background,
            modifier = Modifier.alpha(0f)
        )
    }
}
