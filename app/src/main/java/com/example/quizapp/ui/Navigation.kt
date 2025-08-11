package com.example.quizapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.settings.SettingsScreen
import com.example.quizapp.ui.screens.settings.SettingsViewModel
import com.example.quizapp.ui.screens.quizdetails.QuizDetailsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface QuizRoute {
    @Serializable data object Home : QuizRoute
    @Serializable data class QuizDetails(val quizId: Int) : QuizRoute
    @Serializable data object Settings : QuizRoute
}

@Composable
fun QuizNavGraph(navController: NavHostController) {
    val QuizVm = koinViewModel<QuizViewModel>()
    val QuizState by QuizVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = QuizRoute.Home
    ) {
        composable<QuizRoute.Home> {
            HomeScreen(QuizState, navController)
        }
        composable<QuizRoute.QuizDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<QuizRoute.QuizDetails>()
            val quiz = requireNotNull(QuizState.quizzes.find { it.id == route.quizId })
            QuizDetailsScreen(quiz, navController)
        }

        composable<QuizRoute.Settings> {
            val settingsVm = koinViewModel<SettingsViewModel>()
            SettingsScreen(settingsVm.state, settingsVm::setUsername, navController)
        }
    }
}