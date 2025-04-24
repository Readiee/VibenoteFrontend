package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun BaseInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    isPassword: Boolean = false,
    showToggleVisibility: Boolean = false,
    showClearButton: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    val trailingIcon: @Composable (() -> Unit)? = when {
        // clear text
        value.isNotEmpty() && showClearButton && !isPassword -> {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Очистить",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        // password
        isPassword && showToggleVisibility -> {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icon =
                        if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        else -> null
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            placeholder = { Text(hint, style = MaterialTheme.typography.labelMedium) },
            textStyle = MaterialTheme.typography.bodyMedium,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp)
                .height(56.dp),
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            singleLine = true
        )
        Text(
            text = if (isError && !errorMessage.isNullOrBlank()) errorMessage else " ",
            color = if (isError) MaterialTheme.colorScheme.error else Color.Transparent,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

