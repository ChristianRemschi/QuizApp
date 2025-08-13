package com.example.quizapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.play.PlayScreen
import com.example.quizapp.ui.screens.play.PlayViewModel
import com.example.quizapp.ui.screens.settings.SettingsScreen
import com.example.quizapp.ui.screens.settings.SettingsViewModel
import com.example.quizapp.ui.screens.quizdetails.QuizDetailsScreen
import com.example.quizapp.ui.screens.theme.ThemeScreen
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import io.ktor.http.parametersOf
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

sealed interface QuizRoute {
    @Serializable data object Home : QuizRoute
    @Serializable data class QuizDetails(val quizId: Int) : QuizRoute
    @Serializable data object Settings : QuizRoute
    @Serializable data object Theme : QuizRoute
    @Serializable data class Play(val quizId: Int) : QuizRoute
}

@Composable
fun QuizNavGraph(navController: NavHostController) {
    val quizVm = koinViewModel<QuizViewModel>()
    val quizState by quizVm.state.collectAsStateWithLifecycle()



    quizVm.populateDatabase()

    NavHost(
        navController = navController,
        startDestination = QuizRoute.Home
    ) {
        composable<QuizRoute.Home> {
            HomeScreen(quizState, navController)
        }
        composable<QuizRoute.QuizDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<QuizRoute.QuizDetails>()
            val quiz = requireNotNull(quizState.quizzes.find { it.id.toInt() == route.quizId })
            QuizDetailsScreen(quiz, navController)
        }

        composable<QuizRoute.Settings> {
            val settingsVm = koinViewModel<SettingsViewModel>()
            SettingsScreen(settingsVm.state, settingsVm::setUsername, settingsVm::setPassword, navController, quizVm)
        }
        composable<QuizRoute.Theme> {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            ThemeScreen(themeState, themeViewModel::changeTheme, navController)
        }

        composable<QuizRoute.Play> { backStackEntry ->
            val route = backStackEntry.toRoute<QuizRoute.Play>()

            val playViewModel = koinViewModel<PlayViewModel>()

            PlayScreen(playViewModel,route.quizId, navController)
        }

    }
}
