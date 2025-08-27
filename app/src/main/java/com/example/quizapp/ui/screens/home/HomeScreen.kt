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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.ui.QuizRoute
import com.example.quizapp.ui.QuizState
import com.example.quizapp.ui.composables.AppBar
import com.example.quizapp.ui.composables.ImageWithPlaceholder
import com.example.quizapp.ui.composables.Size

@Composable
fun HomeScreen(
    state: QuizState,
    navController: NavController,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val favoritesOnly by homeViewModel.favoritesOnly.collectAsState()

    val filteredQuizzes = state.quizzes.filter { quiz ->
        // 1. Se la ricerca è vuota, passa sempre
        (searchQuery.isBlank() ||
                quiz.name.contains(searchQuery, ignoreCase = true) ||
                quiz.description.contains(searchQuery, ignoreCase = true))
                &&
                // 2. Se favoritesOnly è attivo, mostra solo i preferiti
                (!favoritesOnly || quiz.isFavorite)
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
                        onClick = { navController.navigate(QuizRoute.QuizDetails(item.id.toInt())) }
                    )
                }
            }
        } else {
            NoItemsPlaceholder(Modifier.padding(contentPadding))
        }
    }
}


@Composable
fun QuizItem(item: Quiz, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(!item.imageUri.isNullOrEmpty()){
                val imageUri = Uri.parse(item.imageUri)
                ImageWithPlaceholder(imageUri, Size.Lg)
            }
            Spacer(Modifier.size(8.dp))
            Text(
                item.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
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
            Icons.Outlined.LocationOn, "Location icon",
            modifier = Modifier.padding(bottom = 16.dp).size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            "No items",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Tap the + button to add a new trip.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
