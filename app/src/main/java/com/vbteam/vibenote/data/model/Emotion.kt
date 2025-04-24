package com.vbteam.vibenote.data.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vbteam.vibenote.R

enum class Emotion(
    val displayName: String,
    @DrawableRes val iconResId: Int?,
    @ColorRes val colorResId: Int,
) {
    ALL("Все", null, R.color.light_gray),
    DRAFT("Черновик", R.drawable.pencil, R.color.light_gray),
    HAPPY("Радость", R.drawable.happy, R.color.green),
    CALM("Спокойствие", R.drawable.calm, R.color.aqua),
    ANGRY("Злость", R.drawable.angry, R.color.red),
    SAD("Печаль", R.drawable.sad, R.color.blue),
    WORRIED("Тревога", R.drawable.worried, R.color.orange),
    CONFUSED("Растерянность", R.drawable.confused, R.color.dark_blue)
}

@get:Composable
val Emotion.color: Color
    get() = when (this) {
        Emotion.ALL, Emotion.DRAFT -> MaterialTheme.colorScheme.surface
        else -> colorResource(id = colorResId)
    }






