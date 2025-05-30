package com.vbteam.vibenote.ui.screens.notes.components

import android.text.BoringLayout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.ui.components.BaseInputField
import com.vbteam.vibenote.ui.components.BaseTopBar
import com.vbteam.vibenote.ui.components.NullAlignTopBar
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@Composable
fun NotesDefaultTopBar(
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    hasNotes: Boolean,
    onSyncClick: (() -> Unit)? = null,
    isSyncing: Boolean = false
) {
    BaseTopBar(
        centerContent = {
            Text(
                text = "Записи",
                style = MaterialTheme.typography.titleLarge
            )
        },
        leftContent = {
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Профиль",
                    modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                )
            }
        },
        rightContent = {
            if (hasNotes) {
                /*
                if (isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else if (onSyncClick != null) {
                    IconButton(onClick = onSyncClick) {
                        Icon(
                            imageVector = Icons.Outlined.CloudSync,
                            contentDescription = "Синхронизировать",
                            modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                        )
                    }
                }
                */

                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Поиск",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                    )
                }
            } else {
                NullAlignTopBar()
            }
        }
    )
}
