package com.example.quizapp.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.models.QuizWithFavorite
import com.example.quizapp.ui.QuizRoute
import com.example.quizapp.ui.QuizState
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.composables.AppBar
import com.example.quizapp.ui.composables.ImageWithPlaceholder
import com.example.quizapp.ui.composables.Size
import com.example.quizapp.ui.toRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    navController: NavController,
    userId: Int?,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    quizViewModel: QuizViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val state by quizViewModel.state.collectAsStateWithLifecycle()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val favoritesOnly by homeViewModel.favoritesOnly.collectAsState()

    val filteredQuizzes = state.quizzes.filter { quiz ->
        (searchQuery.isBlank() ||
                quiz.name.contains(searchQuery, ignoreCase = true) ||
                quiz.description.contains(searchQuery, ignoreCase = true)) &&
                (!favoritesOnly || quiz.isFavorite)
    }

    LaunchedEffect(Unit) {
        quizViewModel.populateDatabase()
    }
    LaunchedEffect(userId) {
        quizViewModel.refreshQuizzes(userId)
    }


    Scaffold(
        topBar = { AppBar(navController, title = "Home", homeViewModel = homeViewModel) }
    ) { contentPadding ->
        if (filteredQuizzes.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
                modifier = Modifier.padding(contentPadding)
            ) {
                items(filteredQuizzes) { item ->
                    QuizItem(
                        item,
                        onClick = { navController.navigate(QuizRoute.QuizDetails(item.id).toRoute()) }
                    )
                }
            }
        } else {
            NoItemsPlaceholder(Modifier.padding(contentPadding))
        }
    }
}



@Composable
fun QuizItem(item: com.example.quizapp.data.models.QuizWithFavorite, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item.imageUri?.let {
                ImageWithPlaceholder(Uri.parse(it), Size.Lg)
            }
            Spacer(Modifier.size(8.dp))
            Text(
                item.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = if (item.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favourite"
            )
        }
    }
}

@Composable
fun NoItemsPlaceholder(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            Icons.Outlined.Quiz, "Quiz icon",
            modifier = Modifier.padding(bottom = 16.dp).size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            "No quizzes",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
