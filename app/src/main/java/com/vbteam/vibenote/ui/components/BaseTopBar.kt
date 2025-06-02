package com.vbteam.vibenote.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview

fun BaseTopBar(
    modifier: Modifier = Modifier,
    leftContent: @Composable RowScope.() -> Unit = {},
    centerContent: @Composable () -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {},
) {
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    TopAppBar(
        expandedHeight = 68.dp,
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = onBackgroundColor
        ),
        title = {
            CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .padding(horizontal = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    centerContent()
                }
            }
        },
        navigationIcon = {
            CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
                Row(
//                    modifier = Modifier
//                        .widthIn(min = 56.dp),
                    content = leftContent
                )
            }
        },
        actions = {
            CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
                Row(
//                    modifier = Modifier
//                        .widthIn(min = 56.dp),
                    content = rightContent
                )
            }
        }
    )
}

