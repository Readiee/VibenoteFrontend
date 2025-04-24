package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    BaseTopBar(
        centerContent = {
            CompactInputField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                hint = "Поиск",
                modifier = Modifier.fillMaxWidth().height(48.dp)
            )
        },
        leftContent = {
            IconButton(onClick = onSearchClose) {
                Icon(Icons.Rounded.ChevronLeft, contentDescription = "Закрыть поиск")
            }
        },
        rightContent = {}
    )
}