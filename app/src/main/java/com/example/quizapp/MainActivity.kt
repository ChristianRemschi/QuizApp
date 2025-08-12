package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.util.Log
import com.example.quizapp.data.models.Theme
import com.example.quizapp.ui.screens.theme.ThemeScreen
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import org.koin.androidx.compose.koinViewModel

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            val themeViewModel = koinViewModel<ThemeViewModel>()
//            val themeState by themeViewModel.state.collectAsStateWithLifecycle()
//            QuizAppTheme(
//                darkTheme = when (themeState.theme) {
//                    Theme.Light -> false
//                    Theme.Dark -> true
//                    Theme.System -> isSystemInDarkTheme()
//                }
//            ) {
//                ThemeScreen(themeState, themeViewModel::changeTheme)
//            }
//        }
//    }
//}


import androidx.navigation.compose.rememberNavController
import com.example.quizapp.ui.QuizNavGraph
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()
            QuizAppTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                val navController = rememberNavController()
                QuizNavGraph(navController)
            }
        }
    }
}