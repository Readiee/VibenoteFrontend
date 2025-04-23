package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vbteam.vibenote.R

@Composable
fun DialogAuthRequirement(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.account_circle_24px),
                    contentDescription = "Профиль",
                    modifier = Modifier.size(48.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Вы не авторизованы",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,

                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Авторизуйтесь, чтобы сохранять записи в облако",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            BaseButton(
                text = "Войти",
                onClick = onConfirmation,
                type = AppButtonType.PRIMARY,
                icon = Icons.AutoMirrored.Rounded.Login,
                modifier = Modifier.height(48.dp)
            )
        },
        dismissButton = {
            BaseButton(
                text = "Не сейчас",
                onClick = onDismiss,
                type = AppButtonType.SECONDARY,
                modifier = Modifier.height(48.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp)
    )
}