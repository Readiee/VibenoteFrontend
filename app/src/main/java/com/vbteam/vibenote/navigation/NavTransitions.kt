package com.vbteam.vibenote.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun defaultEnterTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(300)) + slideInHorizontally(
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    ) { it }
}

fun defaultExitTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
        animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
    ) { -it }
}

fun defaultPopEnterTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(300)) + slideInHorizontally(
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    ) { -it }
}

fun defaultPopExitTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
        animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
    ) { it }
}

//    EnterTransition.None
//    ExitTransition.None
//    EnterTransition.None
//    ExitTransition.None


