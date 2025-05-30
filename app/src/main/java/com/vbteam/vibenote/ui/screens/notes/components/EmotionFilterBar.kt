package com.vbteam.vibenote.ui.screens.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@Composable
fun EmotionFilterBar(
    selectedFilter: Emotion,
    onFilterSelected: (Emotion) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(Emotion.entries) { filter ->
            FilterChip(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    iconColor = MaterialTheme.colorScheme.onBackground,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null,
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(
                    text = filter.displayName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(min = 60.dp).padding(vertical = 10.dp),
                    style = MaterialTheme.typography.bodySmall
                ) },
                leadingIcon = {
                    filter.iconResId?.let { iconRes ->
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Emotion: $filter",
                            modifier = Modifier.size(LocalAppDimens.current.iconSizeSmall),
                            tint = if (selectedFilter == filter) 
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f)
                            else 
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                },
            )
        }
    }
}

