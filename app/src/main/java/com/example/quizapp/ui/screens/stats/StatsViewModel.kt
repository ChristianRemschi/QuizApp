package com.example.quizapp.ui.screens.stats

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.repositories.QuizRepository
import kotlinx.coroutines.launch

class StatsViewModel(private val repository: QuizRepository) : ViewModel() {
    private val _userScores = mutableStateOf<List<Pair<String, Float>>>(emptyList())
    val userScores: State<List<Pair<String, Float>>> = _userScores

    private val _scoreDistribution = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val scoreDistribution: State<List<Pair<String, Int>>> = _scoreDistribution

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadUserData(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Ottieni i punteggi
                val scores = repository.getScoresForPerson(userId)

                // Per ogni score, ottieni il quiz corrispondente
                val scoresWithQuizNames = scores.map { score ->
                    val quiz = repository.getQuiz(score.quizId)
                    quiz.name to score.score.toFloat()
                }

                _userScores.value = scoresWithQuizNames

                // Calcola la distribuzione
                _scoreDistribution.value = listOf(
                    "Excellent" to scores.count { it.score >= 8 },
                    "Good" to scores.count { it.score in 5..7 },
                    "Poor" to scores.count { it.score < 5 }
                )

            } catch (e: Exception) {
                // Gestisci errore
                _userScores.value = emptyList()
                _scoreDistribution.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}