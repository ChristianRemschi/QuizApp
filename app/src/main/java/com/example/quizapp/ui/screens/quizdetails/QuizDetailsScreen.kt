package com.example.quizapp.ui.screens.quizdetails

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.ui.QuizRoute
import com.example.quizapp.ui.QuizState
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.composables.AppBar
import com.example.quizapp.ui.composables.ImageWithPlaceholder
import com.example.quizapp.ui.composables.Size

@Composable
fun QuizDetailsScreen(
    quizId: Int,
    navController: NavController,
    quizVm: QuizViewModel
) {
    val ctx = LocalContext.current
    val userId by quizVm.authStateManager.currentUserId.collectAsStateWithLifecycle(initialValue = null)
    val quizzesState by quizVm.state.collectAsStateWithLifecycle(initialValue = QuizState(quizzes = emptyList()))

    // Prendiamo il quiz aggiornato dallo stato globale
    val currentQuiz = quizzesState.quizzes.firstOrNull { it.id == quizId }

    if (currentQuiz == null) {
        Text("Quiz non trovato")
        return
    }

    Scaffold(
        topBar = { AppBar(navController, title = "Quiz Details") },
        floatingActionButton = {
            val isFavorite = currentQuiz.isFavorite
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiary,
                onClick = {
                    val currentUserId = userId
                    if (currentUserId != null) {
                        val quizToToggle = Quiz(
                            id = currentQuiz.id,
                            name = currentQuiz.name,
                            description = currentQuiz.description,
                            imageUri = currentQuiz.imageUri,
                            isComplete = currentQuiz.isComplete
                        )
                        quizVm.toggleFavorite(currentUserId, quizToToggle)
                    } else {
                        Log.d("QUIZ", "User not logged in")
                    }
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favourite"
                )
            }
        },
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(contentPadding).padding(12.dp).fillMaxSize()
        ) {
            currentQuiz.imageUri?.let {
                ImageWithPlaceholder(Uri.parse(it), Size.Lg)
            }

            Text(currentQuiz.name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.size(8.dp))
            Text(currentQuiz.description, style = MaterialTheme.typography.bodyMedium)
            IconButton(onClick = { navController.navigate(QuizRoute.Play(currentQuiz.id)) }) {
                Icon(Icons.Outlined.PlayCircle, "Play")
            }
        }
    }
}
