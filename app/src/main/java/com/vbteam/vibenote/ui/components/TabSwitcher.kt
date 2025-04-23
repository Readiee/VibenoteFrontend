package com.vbteam.vibenote.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TabSwitcher(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    animationDurationMillis: Int = 300,
    tabContent: @Composable (Int) -> Unit
) {
    AnimatedContent(
        targetState = selectedTabIndex,
        transitionSpec = {
            val initialTab = initialState
            val targetTab = targetState
            val direction = if (targetTab > initialTab) 1 else -1

            // кривой
            ContentTransform(
                targetContentEnter = slideInHorizontally(
                    animationSpec = tween(durationMillis = animationDurationMillis)
                ) { fullWidth -> direction * fullWidth } +
                        fadeIn(animationSpec = tween(durationMillis = animationDurationMillis / 2)),
                initialContentExit = slideOutHorizontally(
                    animationSpec = tween(durationMillis = animationDurationMillis)
                ) { fullWidth -> -direction * fullWidth } +
                        fadeOut(animationSpec = tween(durationMillis = animationDurationMillis / 2))
            )
        },
        modifier = modifier.fillMaxSize()
    ) { tabIndex ->
        Box(modifier = Modifier.fillMaxSize()) {
            tabContent(tabIndex)
        }
    }
}