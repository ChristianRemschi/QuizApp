package com.example.quizapp.ui.screens.quizdetails

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.ui.QuizRoute
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.composables.AppBar
import com.example.quizapp.ui.composables.ImageWithPlaceholder
import com.example.quizapp.ui.composables.Size
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuizDetailsScreen(quiz: Quiz, navController: NavController) {
    val ctx = LocalContext.current
    val quizVm = koinViewModel<QuizViewModel>()

    fun shareDetails() { //TODO condividi dettagli? ci serve? ora non la stiamo usando
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, quiz.name)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Quiz")
        if (shareIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(shareIntent)
        }
    }

    Scaffold(
        topBar = { AppBar(navController, title = "Quiz Details") },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiary,
                onClick = {
                    quizVm.toggleFavorite(quiz)
                }
            ) {
                Icon(
                    if (quiz.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
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
            if(!quiz.imageUri.isNullOrEmpty()){
                val imageUri = Uri.parse(quiz.imageUri)
                ImageWithPlaceholder(imageUri, Size.Lg)
            }

            Text(
                quiz.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.size(8.dp))
            Text(
                quiz.description,
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = { navController.navigate(QuizRoute.Play(quiz.id)) }) {
                Icon(Icons.Outlined.PlayCircle, "Play")
            }
        }
    }
}