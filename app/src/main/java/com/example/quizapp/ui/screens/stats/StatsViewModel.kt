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

    // Statistiche riepilogative
    private val _quizTypeStats = mutableStateOf<Map<String, QuizTypeStats>>(emptyMap())
    val quizTypeStats: State<Map<String, QuizTypeStats>> = _quizTypeStats

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadUserData(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val scores = repository.getScoresForPerson(userId)

                val allScoresWithNames = scores.map { score ->
                    val quiz = repository.getQuiz(score.quizId)
                    quiz.name to score.score.toFloat() //TODO add data se vogliamo poi aggiungerla nel database
                }
                _allUserScores.value = allScoresWithNames

                val scoresByQuizType = mutableMapOf<String, MutableList<Pair<String, Float>>>()
                val statsByQuizType = mutableMapOf<String, QuizTypeStats>()

                scores.forEach { score ->
                    val quiz = repository.getQuiz(score.quizId)
                    val quizType = quiz.name

                    val scoreEntry = quiz.name to score.score.toFloat()//TODO add data
                    scoresByQuizType.getOrPut(quizType) { mutableListOf() }.add(scoreEntry)

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

                val finalStats = statsByQuizType.mapValues { (_, stats) ->
                    stats.copy(averageScore = stats.totalScore / stats.totalAttempts.toFloat())
                }
                _quizTypeStats.value = finalStats

            } catch (e: Exception) {
                _allUserScores.value = emptyList()
                _userScoresByQuizType.value = emptyMap()
                _quizTypeStats.value = emptyMap()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class QuizTypeStats(
    val quizType: String,
    val totalAttempts: Int,
    val totalScore: Int,
    val averageScore: Float,
    val maxScore: Int,
    val minScore: Int
)