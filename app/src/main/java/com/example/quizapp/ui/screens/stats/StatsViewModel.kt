package com.example.quizapp.ui.screens.stats

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.repositories.QuizRepository
import kotlinx.coroutines.launch

class StatsViewModel(private val repository: QuizRepository) : ViewModel() {
    // Grafico complessivo (tutti i punteggi in ordine temporale)
    private val _allUserScores = mutableStateOf<List<Pair<String, Float>>>(emptyList())
    val allUserScores: State<List<Pair<String, Float>>> = _allUserScores

    // Dati separati per tipo di quiz (per i grafici individuali)
    private val _userScoresByQuizType = mutableStateOf<Map<String, List<Pair<String, Float>>>>(emptyMap())
    val userScoresByQuizType: State<Map<String, List<Pair<String, Float>>>> = _userScoresByQuizType

    // Distribuzione dei punteggi
    private val _scoreDistribution = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val scoreDistribution: State<List<Pair<String, Int>>> = _scoreDistribution

    // Statistiche riepilogative
    private val _quizTypeStats = mutableStateOf<Map<String, QuizTypeStats>>(emptyMap())
    val quizTypeStats: State<Map<String, QuizTypeStats>> = _quizTypeStats

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadUserData(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Ottieni i punteggi
                val scores = repository.getScoresForPerson(userId)

                // Prepara dati per il grafico complessivo
                val allScoresWithNames = scores.map { score ->
                    val quiz = repository.getQuiz(score.quizId)
                    quiz.name to score.score.toFloat() //TODO add data
                }
                _allUserScores.value = allScoresWithNames

                // Raggruppa i punteggi per tipo di quiz
                val scoresByQuizType = mutableMapOf<String, MutableList<Pair<String, Float>>>()
                val statsByQuizType = mutableMapOf<String, QuizTypeStats>()

                scores.forEach { score ->
                    val quiz = repository.getQuiz(score.quizId)
                    val quizType = quiz.name ?: "Generale" // Usa "Generale" se il tipo non è specificato

                    // Aggiungi al gruppo per tipo di quiz
                    val scoreEntry = quiz.name to score.score.toFloat()//TODO add data
                    scoresByQuizType.getOrPut(quizType) { mutableListOf() }.add(scoreEntry)

                    // Aggiorna le statistiche per questo tipo di quiz
                    val currentStats = statsByQuizType.getOrPut(quizType) {
                        QuizTypeStats(quizType, 0, 0, 0f, 0, 0)
                    }
                    statsByQuizType[quizType] = currentStats.copy(
                        totalAttempts = currentStats.totalAttempts + 1,
                        totalScore = currentStats.totalScore + score.score,
                        maxScore = maxOf(currentStats.maxScore, score.score),
                        minScore = if (currentStats.totalAttempts == 0) score.score
                        else minOf(currentStats.minScore, score.score)
                    )
                }

                _userScoresByQuizType.value = scoresByQuizType

                // Calcola le medie per ogni tipo di quiz
                val finalStats = statsByQuizType.mapValues { (_, stats) ->
                    stats.copy(averageScore = stats.totalScore / stats.totalAttempts.toFloat())
                }
                _quizTypeStats.value = finalStats

                // Calcola la distribuzione complessiva
                _scoreDistribution.value = listOf(
                    "Excellent" to scores.count { it.score >= 8 },
                    "Good" to scores.count { it.score in 5..7 },
                    "Poor" to scores.count { it.score < 5 }
                )

            } catch (e: Exception) {
                // Gestisci errore
                _allUserScores.value = emptyList()
                _userScoresByQuizType.value = emptyMap()
                _scoreDistribution.value = emptyList()
                _quizTypeStats.value = emptyMap()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Classe per le statistiche di ogni tipo di quiz
data class QuizTypeStats(
    val quizType: String,
    val totalAttempts: Int,
    val totalScore: Int,
    val averageScore: Float,
    val maxScore: Int,
    val minScore: Int
)