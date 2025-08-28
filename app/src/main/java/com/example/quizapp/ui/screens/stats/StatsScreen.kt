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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = viewModel<StatsViewModel>(),
    userId: Int,
    navController: NavController
) {
    viewModel.loadUserData(userId)
    val allUserScores by viewModel.allUserScores
    val userScoresByQuizType by viewModel.userScoresByQuizType
    val quizTypeStats by viewModel.quizTypeStats
    val isLoading by viewModel.isLoading

    Scaffold(
        topBar = {
            AppBar(navController, title = "Stats")
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // GRAFICO COMPLESSIVO
                if (allUserScores.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "General Performance",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                BarChartComponent(
                                    data = allUserScores,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    title = "All Quizzes"
                                )

                                val overallStats = calculateOverallStats(allUserScores)
                                ScoreStats(
                                    stats = overallStats,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }

                // GRAFICI PER QUIZ
                if (userScoresByQuizType.isNotEmpty()) {
                    item {
                        Text(
                            text = "Performance by Quiz Type",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    userScoresByQuizType.forEach { (quizType, scores) ->
                        item(key = quizType) {
                            val stats = quizTypeStats[quizType]
                            QuizTypeSection(
                                quizType = quizType,
                                scores = scores,
                                stats = stats,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                if (allUserScores.isEmpty() && userScoresByQuizType.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "No data available",
                                modifier = Modifier
                                    .padding(32.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun calculateOverallStats(scores: List<Pair<String, Float>>): QuizTypeStats {
    val scoreValues = scores.map { it.second }
    val totalScore = scoreValues.sum().toInt()
    return QuizTypeStats(
        quizType = "Overall",
        totalScore = totalScore,
        totalAttempts = scores.size,
        averageScore = scoreValues.average().toFloat(),
        maxScore = scoreValues.maxOrNull()?.toInt() ?: 0,
        minScore = scoreValues.minOrNull()?.toInt() ?: 0
    )
}

@Composable
fun QuizTypeSection(
    quizType: String,
    scores: List<Pair<String, Float>>,
    stats: QuizTypeStats?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = quizType,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            BarChartComponent(
                data = scores,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            stats?.let {
                ScoreStats(stats = it, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun ScoreStats(stats: QuizTypeStats, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(label = "Attempts", value = stats.totalAttempts.toString())
        StatItem(label = "Mean", value = "%.1f".format(stats.averageScore))
        StatItem(label = "Max", value = stats.maxScore.toString())
        StatItem(label = "Min", value = stats.minScore.toString())
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}