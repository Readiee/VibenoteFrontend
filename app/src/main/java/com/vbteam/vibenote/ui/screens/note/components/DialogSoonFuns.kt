package com.vbteam.vibenote.ui.screens.note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.vbteam.vibenote.ui.components.BaseButton

@Composable
fun DialogSoonFuns(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_rocket),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Новые функции скоро!",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Загрузка селфи, запись голоса и их ИИ-анализ уже на подходе.\nСледите за обновлениями!",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            BaseButton(
                modifier = Modifier.height(48.dp).fillMaxWidth(),
                text = "Жду с нетерпением!",
                onClick = onDismiss,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp)
    )
}