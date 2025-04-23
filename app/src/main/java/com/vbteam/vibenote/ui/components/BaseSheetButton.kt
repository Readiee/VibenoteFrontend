package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@Composable
fun BaseSheetButton(icon: ImageVector, text: String, labelText: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(100.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = labelText,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}