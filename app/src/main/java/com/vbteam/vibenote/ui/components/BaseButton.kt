package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.ui.theme.LocalAppDimens

enum class AppButtonType {
    PRIMARY,
    SECONDARY,
    ERROR,
}

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.PRIMARY,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val backgroundColor: Color
    val contentColor: Color

    when (type) {
        AppButtonType.PRIMARY -> {
            backgroundColor =
                if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.3f
                )
            contentColor = MaterialTheme.colorScheme.onPrimary
        }

        AppButtonType.SECONDARY -> {
            backgroundColor = MaterialTheme.colorScheme.secondary
            contentColor = MaterialTheme.colorScheme.onBackground
        }

        AppButtonType.ERROR -> {
            backgroundColor = MaterialTheme.colorScheme.error
            contentColor = Color.White
        }
    }

    val shape = RoundedCornerShape(50)

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = contentColor.copy(alpha = 0.80f)
        ),
        shape = shape,
        modifier = modifier
            .height(56.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium),
                tint = if (type == AppButtonType.PRIMARY) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
