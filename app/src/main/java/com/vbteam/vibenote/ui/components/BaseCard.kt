package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseCard (modifier: Modifier = Modifier, content: @Composable (ColumnScope.() -> Unit)) {
    Card(
        shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
            .padding(horizontal =  16.dp, vertical = 20.dp)
            .fillMaxWidth(),
        content = content
    )
}