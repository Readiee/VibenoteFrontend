package com.vbteam.vibenote

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.vbteam.vibenote.navigation.NavGraph
import com.vbteam.vibenote.ui.theme.VibeNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        // splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f
            )
            fadeOut.duration = 100L

            fadeOut.doOnEnd { splashScreenView.remove() }
            fadeOut.start()
        }

        super.onCreate(savedInstanceState)
        setContent {
            VibeNoteTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
