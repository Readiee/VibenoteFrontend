package com.vbteam.vibenote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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
fun VibenoteTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme(
            primary = colorResource(id = R.color.accent),
            secondary = colorResource(id = R.color.darkTheme_secondary),

            onPrimary = colorResource(id = R.color.white),
            onSecondary = colorResource(id = R.color.black),

            onSurfaceVariant = colorResource(id = R.color.darkTheme_onSurfaceVariant),

            surface = colorResource(id = R.color.darkTheme_surface),
            onSurface = colorResource(id = R.color.gray_200),

            background = colorResource(id = R.color.darkTheme_background),
            onBackground = colorResource(id = R.color.white),

            error = colorResource(id = R.color.red),
        )
    } else {
        lightColorScheme(
            primary = colorResource(id = R.color.accent),
            secondary = colorResource(id = R.color.light_gray),

            onPrimary = colorResource(id = R.color.white),
            onSecondary = colorResource(id = R.color.black),

            onSurfaceVariant = colorResource(id = R.color.gray_400),

            surface = colorResource(id = R.color.light_gray),
            onSurface = colorResource(id = R.color.gray_700),

            background = colorResource(id = R.color.white),
            onBackground = colorResource(id = R.color.black),

            error = colorResource(id = R.color.red),
        )
    }

    CompositionLocalProvider(
        LocalAppDimens provides AppDimens()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
