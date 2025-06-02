package com.vbteam.vibenote.ui.screens.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.R
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
            Image(
                painter = painterResource(id = R.drawable.logo_ext),
                contentDescription = null,
                modifier = Modifier
                    .height(32.dp)
            )
            /*
            Text(
                text = "Записи",
                style = MaterialTheme.typography.titleLarge
            )
             */
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
