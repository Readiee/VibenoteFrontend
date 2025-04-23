package com.vbteam.vibenote.ui.screens.notes.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.vbteam.vibenote.ui.components.BaseTopBar
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@Composable
fun SelectionTopBar(
    selectedCount: Int,
    onDeleteClick: () -> Unit,
    onClearSelection: () -> Unit
) {
    BaseTopBar(
        leftContent = {
            IconButton(onClick = { onClearSelection() }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Сбросить выделение",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                )
            }
        }, centerContent = {
            Text(
                text = selectedCount.toString(),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        rightContent = {
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                )
            }
        }
    )
}
