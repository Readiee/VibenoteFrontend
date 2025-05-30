package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(
            hostState = hostState,
            modifier = Modifier.padding(16.dp)
        ) { snackbarData ->
            val messageParts = snackbarData.visuals.message.split("\n")
            if (messageParts.size >= 3) {
                val type = try {
                    SnackbarType.valueOf(messageParts[0])
                } catch (e: IllegalArgumentException) {
                    SnackbarType.ERROR // Fallback тип
                }
                
                CustomSnackbar(
                    data = SnackbarData(
                        title = messageParts[1],
                        text = messageParts[2],
                        type = type
                    )
                )
            } else {
                Snackbar { snackbarData.visuals.message }
            }
        }
    }
} 