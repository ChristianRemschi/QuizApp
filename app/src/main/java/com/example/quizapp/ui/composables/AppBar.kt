package com.example.quizapp.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.quizapp.ui.QuizRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController, title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Go Back")
                }
            }
        },
        actions = {
//            if (title == "Home") {
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(Icons.Outlined.Search, contentDescription = "Search")
//                }
//            }
            if (title != "Login" && title != "Profile") {
                IconButton(onClick = { navController.navigate(QuizRoute.Settings) }) {
                    Icon(Icons.Outlined.Person, "Profile")
                }
            }
            if (title != "Theme") {
                IconButton(onClick = { navController.navigate(QuizRoute.Theme) }) {
                    Icon(Icons.Outlined.Contrast, "Theme")
                }
            }
            if (title != "Home") {
                IconButton(onClick = { navController.navigate(QuizRoute.Home) }) {
                    Icon(Icons.Outlined.Home, "Home")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}