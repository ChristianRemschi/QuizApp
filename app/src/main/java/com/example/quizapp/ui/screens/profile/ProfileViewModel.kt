package com.example.quizapp.ui.screens.profile

import androidx.collection.emptyIntList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun loadUserData(userId: Int) {
        viewModelScope.launch {
            // Carica i dati dell'utente
            _userData.value = quizRepository.getPersonById(userId)

            // Carica i punteggi dell'utente con i dettagli dei quiz
            val scores = quizRepository.getScoresForPerson(userId)
            val quizzesWithScores = scores.map { score ->
                val quiz = quizRepository.getQuiz(score.quizId)
                Pair(quiz, score)
            }
            _userScores.value = quizzesWithScores
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
}
