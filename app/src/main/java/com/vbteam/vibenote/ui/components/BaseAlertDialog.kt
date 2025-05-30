package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseAlertDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    confirmButtonText: String,
    confirmButtonType: AppButtonType,
    dismissButtonText: String,
    text: String,
    title: String
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        dismissButton = {
            Row () {
                if (dismissButtonText.isNotBlank()) {
                    BaseButton(
                        text = dismissButtonText,
                        onClick = onDismiss,
                        type = AppButtonType.SECONDARY,
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                BaseButton(
                    type = confirmButtonType,
                    text = confirmButtonText,
                    onClick = onConfirmation,
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                )
            }
        },
        confirmButton = {}
    )
}