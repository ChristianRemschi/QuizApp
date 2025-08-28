package com.example.quizapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.database.Badge
import com.example.quizapp.data.database.Person
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.Score
import com.example.quizapp.data.repositories.AuthStateManager
import com.example.quizapp.data.repositories.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val quizRepository: QuizRepository,
    private val authStateManager: AuthStateManager
) : ViewModel() {
    private val _userData = MutableStateFlow<Person?>(null)
    val userData: StateFlow<Person?> = _userData.asStateFlow()

    private val _userScores = MutableStateFlow<List<Pair<Quiz, Score>>>(emptyList())
    val userScores: StateFlow<List<Pair<Quiz, Score>>> = _userScores.asStateFlow()

    private val _topUserScores = MutableStateFlow<List<Pair<Quiz, Score>>>(emptyList())
    val topUserScores: StateFlow<List<Pair<Quiz, Score>>> = _topUserScores.asStateFlow()

    fun loadUserData(userId: Int) {
        viewModelScope.launch {
            _userData.value = quizRepository.getPersonById(userId)

            val scores = quizRepository.getScoresForPerson(userId)
            val quizzesWithScores = scores.map { score ->
                val quiz = quizRepository.getQuiz(score.quizId)
                Pair(quiz, score)
            }
            _userScores.value = quizzesWithScores
            loadUserBadges(userId)
        }
    }

    fun getTop3(userId: Int) {
        viewModelScope.launch {
            val top3Scores = quizRepository.getBestScoresForPerson(userId)
            val topQuizzesWithScores = top3Scores.map { topScore ->
                val topQuiz = quizRepository.getQuiz(topScore.quizId)
                Pair(topQuiz, topScore)
            }
            _topUserScores.value = topQuizzesWithScores
        }
    }

    fun updateProfile(name: String, biography: String?, photoUri: String?) {
        viewModelScope.launch {
            val currentUser = _userData.value ?: return@launch
            val updatedUser = currentUser.copy(
                name = name,
                biography = biography,
                photo = photoUri
            )
            quizRepository.updatePerson(updatedUser)
            _userData.value = updatedUser
        }
    }
    fun logout() {
        viewModelScope.launch {
            authStateManager.logout()
        }
    }

    private val _userBadges = MutableStateFlow<List<Badge>>(emptyList())
    val userBadges: StateFlow<List<Badge>> = _userBadges

    private fun loadUserBadges(userId: Int) {
        viewModelScope.launch {
            _userBadges.value = quizRepository.getBadgesForPerson(userId)
        }
    }
}
