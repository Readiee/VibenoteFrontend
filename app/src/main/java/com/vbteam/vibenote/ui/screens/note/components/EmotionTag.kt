package com.vbteam.vibenote.ui.screens.note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.Tag
import com.vbteam.vibenote.data.model.color

@Composable
fun EmotionTag(
    tag: Tag,
    emotion: Emotion,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(emotion.color)
            .padding(horizontal = 10.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        emotion.iconResId?.let { iconResId ->
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = emotion.displayName,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
            )
        }
        Text(
            text = emotion.displayName,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "${tag.value}%",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
        )
    }
} 