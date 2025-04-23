package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.TextField
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
    showClearButton: Boolean = true
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

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint, style = MaterialTheme.typography.labelMedium) },
        textStyle = MaterialTheme.typography.labelMedium,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)
            .heightIn(max = 56.dp),
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
}
