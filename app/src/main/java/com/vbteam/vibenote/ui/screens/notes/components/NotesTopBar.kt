package com.vbteam.vibenote.ui.screens.notes.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.ui.components.BaseInputField
import com.vbteam.vibenote.ui.components.BaseTopBar
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@Composable
fun NotesTopBar(
    onProfileClick: () -> Unit,
    selectedCount: Int,
    onDeleteClick: () -> Unit,
    onClearSelection: () -> Unit,
    isSearching: Boolean,
    onSearchClose: () -> Unit,
    onSearchClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    BaseTopBar(
        leftContent = {
            if (selectedCount > 0) {
                IconButton(onClick = onClearSelection) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Отменить выбор",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                    )
                }
            } else if (isSearching) {
                IconButton(onClick = onSearchClose) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = "Назад из поиска",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                    )
                }
            } else {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Профиль",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                    )
                }
            }
        },
        centerContent = {
            if (selectedCount > 0) {
                Text(
                    text = selectedCount.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
            } else if (isSearching) {
                BaseInputField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    hint = "Найти запись",
                    modifier = Modifier.height(48.dp),
                    showClearButton = true
                )
            } else {
                Text(
                    text = "Записи",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

        },
        rightContent = {
            if (selectedCount > 0) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Удалить выбранные",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                    )
                }
            } else if (isSearching) { null
            } else {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Поиск",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                    )
                }
            }
        }
    )
}