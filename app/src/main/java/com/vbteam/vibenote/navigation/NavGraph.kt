package com.vbteam.vibenote.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vbteam.vibenote.ui.screens.auth.SignInScreenUI
import com.vbteam.vibenote.ui.screens.auth.SignUpScreenUI
import com.vbteam.vibenote.ui.screens.note.NoteRoute
import com.vbteam.vibenote.ui.screens.notes.NotesRoute
import com.vbteam.vibenote.ui.screens.profile.ProfileScreenUI
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.navArgument

sealed class Routes(val route: String) {
    object NotesScreen : Routes("notes")
    object SignInScreen : Routes("sign_in")
    object SignUpScreen : Routes("sign_up")
    object ProfileScreen : Routes("profile")
    object CreateNoteRoute : Routes("note")
    object NoteRoute : Routes("note/{noteId}")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.NotesScreen.route
    ) {
        composable(
            route = Routes.NotesScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { defaultPopExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultExitTransition() }
        ) {
            NotesRoute(navController = navController)
        }

        composable(
            route = Routes.SignInScreen.route,
            enterTransition = { defaultEnterTransition() },
            exitTransition = { defaultExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            SignInScreenUI(navController)
        }

        composable(
            route = Routes.SignUpScreen.route,
            enterTransition = { defaultEnterTransition() },
            exitTransition = { defaultExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            SignUpScreenUI(navController)
        }

        composable(
            route = Routes.ProfileScreen.route,
            enterTransition = { defaultEnterTransition() },
            exitTransition = { defaultExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            ProfileScreenUI(navController)
        }

        composable(
            route = Routes.CreateNoteRoute.route,
            enterTransition = { defaultEnterTransition() },
            exitTransition = { defaultExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            NoteRoute(navController = navController)
        }

        composable(
            route = Routes.NoteRoute.route,
            arguments = listOf(navArgument("noteId") { nullable = true }),
            enterTransition = { defaultEnterTransition() },
            exitTransition = { defaultExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteRoute(navController = navController, noteId = noteId)
        }
    }
}
