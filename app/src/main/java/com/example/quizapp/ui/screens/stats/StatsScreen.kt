package com.example.quizapp.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizapp.ui.composables.AppBar
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = viewModel<StatsViewModel>(),
    userId: Int,
    navController: NavController
) {
    viewModel.loadUserData(userId)
    val userScores by viewModel.userScores
    val scoreDistribution by viewModel.scoreDistribution

    Scaffold(
        topBar = {
            AppBar(navController, title = "Stats")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Performance Quiz",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (userScores.isNotEmpty()) {
                item {
                    BarChartComponent(
                        data = userScores,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

//            item {
//                Text(
//                    text = "Distribuzione Punteggi",
//                    style = MaterialTheme.typography.headlineMedium,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }

//            if (scoreDistribution.isNotEmpty()) {
//                item {
//                    PieChartComponent(
//                        data = scoreDistribution,
//                        modifier = Modifier
//                            .align(Alignment.CenterHorizontally)
//                            .padding(16.dp)
//                    )
//                }
//
//                item {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        scoreDistribution.forEach { (category, count) ->
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                modifier = Modifier.padding(4.dp)
//                            ) {
//                                Box(
//                                    modifier = Modifier
//                                        .size(16.dp)
//                                        .background(
//                                            when(category) {
//                                                "Excellent" -> Color.Green
//                                                "Good" -> Color.Yellow
//                                                else -> Color.Red
//                                            }
//                                        )
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text("$category: $count quiz")
//                            }
//                        }
//                    }
//                }
//            }

            if (userScores.isEmpty() && scoreDistribution.isEmpty()) {
                item {
                    Text(
                        text = "No data available",
                        modifier = Modifier
                            .padding(32.dp)
                    )
                }
            }
        }
    }
}