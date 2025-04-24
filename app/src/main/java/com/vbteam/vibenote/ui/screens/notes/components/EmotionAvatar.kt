package com.vbteam.vibenote.ui.screens.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.R
import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.color
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@Composable
fun EmotionAvatar(
    emotion: Emotion,
    modifier: Modifier = Modifier
) {
    val iconResId = emotion.iconResId
    val backgroundColor = emotion.color

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        iconResId?.let {
            if (it == R.drawable.pencil)
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "Emotion: $emotion",
                    modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                )
            else
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Emotion: $emotion",
                    modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                )
        }
    }
}

