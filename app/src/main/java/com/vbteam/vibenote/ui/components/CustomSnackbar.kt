package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.R

enum class SnackbarType {
    ERROR,
    WARNING,
    SUCCESS
}

data class SnackbarData(
    val title: String,
    val text: String,
    val type: SnackbarType
)

@Composable
fun CustomSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (data.type) {
        SnackbarType.ERROR -> MaterialTheme.colorScheme.background
        SnackbarType.WARNING -> MaterialTheme.colorScheme.background
        SnackbarType.SUCCESS -> MaterialTheme.colorScheme.background
    }

    val contentColor = when (data.type) {
        SnackbarType.ERROR -> MaterialTheme.colorScheme.onBackground
        SnackbarType.WARNING -> MaterialTheme.colorScheme.onBackground
        SnackbarType.SUCCESS -> MaterialTheme.colorScheme.onBackground
    }

    val icon = when (data.type) {
        SnackbarType.ERROR -> R.drawable.illustration_cross
        SnackbarType.WARNING -> R.drawable.illustration_warning
        SnackbarType.SUCCESS -> R.drawable.illustration_ok
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            .border(
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp, 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .aspectRatio(1f)
        )

        Column {
            Text(
                text = data.title,
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = data.text,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor
            )
        }
    }
}

suspend fun SnackbarHostState.showCustomSnackbar(
    title: String,
    message: String,
    type: SnackbarType,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showSnackbar(
        message = "${type.name}\n$title\n$message",
        duration = duration,
        withDismissAction = true
    )
} 