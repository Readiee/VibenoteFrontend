package com.vbteam.vibenote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vbteam.vibenote.R

// Загрузка шрифтов
val InterFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val DaysOneFontFamily = FontFamily(
    Font(R.font.days_one)
)

// Типография
val typography = Typography(
    titleLarge = TextStyle(
        fontFamily = DaysOneFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DaysOneFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = DaysOneFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = DaysOneFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DaysOneFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    )
)

data class AppDimens(
    val iconSizeSmall: Dp = 20.dp,
    val iconSizeMedium: Dp = 28.dp,
    val iconSizeLarge: Dp = 36.dp
)

val LocalAppDimens = staticCompositionLocalOf { AppDimens() }

@Composable
fun VibeNoteTheme(content: @Composable () -> Unit) {
    val lightColors = lightColorScheme(
        primary = colorResource(id = R.color.accent),  // Accent
        secondary = colorResource(id = R.color.gray_400),  // Green

        onPrimary = colorResource(id = R.color.white),
        onSecondary = colorResource(id = R.color.black),

        onSurfaceVariant = colorResource(id = R.color.gray_400),

        surface = colorResource(id = R.color.light_gray),  // Цвет фона
        onSurface = colorResource(id = R.color.gray_700),  // Цвет текста на фоне

        background = colorResource(id = R.color.white),  // Общий фон
        onBackground = colorResource(id = R.color.black),  // Текст на фоне

        error = colorResource(id = R.color.red),  // Цвет для ошибок
    )
    CompositionLocalProvider(
        LocalAppDimens provides AppDimens()
    ) {
        MaterialTheme(
            colorScheme = lightColors,
            typography = typography,
            content = content
        )
    }
}
