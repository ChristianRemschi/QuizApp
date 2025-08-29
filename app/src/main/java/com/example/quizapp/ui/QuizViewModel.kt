package com.example.quizapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.database.FavoriteQuiz
import com.example.quizapp.data.database.Person
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.models.QuizWithFavorite
import com.example.quizapp.data.repositories.QuizRepository
import com.example.quizapp.data.repositories.AuthStateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class QuizState(val quizzes: List<com.example.quizapp.data.models.QuizWithFavorite>)

interface QuizActions {
    fun addQuiz(quiz: Quiz): Job
    fun removeQuiz(quiz: Quiz): Job
    fun toggleComplete(quiz: Quiz): Job
}

class QuizViewModel(
    private val repository: QuizRepository,
    val authStateManager: AuthStateManager
) : ViewModel() {
    private val _state = MutableStateFlow(QuizState(emptyList()))
    val state: StateFlow<QuizState> = _state


    val actions = object : QuizActions { //TODO penso sia inutile
        override fun addQuiz(quiz: Quiz) = viewModelScope.launch {
            repository.upsert(quiz)
        }
        override fun removeQuiz(quiz: Quiz) = viewModelScope.launch {
            repository.delete(quiz)
        }
        override fun toggleComplete(quiz: Quiz) = viewModelScope.launch {
            repository.upsert(quiz.copy(isComplete = !quiz.isComplete))
        }
    }
    private val quizDao = repository

    fun populateDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            if (quizDao.getQuizzesCount() == 0) {
                quizDao.populateSampleData()
            }
            refreshQuizzes(userId = null)
        }
    }

    fun createAccount(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {

            val existingUser = quizDao.getByUsername(username)
            if (existingUser != null) {
                onResult(false)
            } else {
                val newPerson = Person(
                    name = username,
                    password = password,
                    photo = "",
                    biography = ""
                )
                repository.insertPerson(newPerson)
                onResult(true)
            }
        }
    }
    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getByUsername(username)
            if (user != null && user.password == password) {
                authStateManager.setLoggedInUser(user.id)
                refreshQuizzes(user.id) // carica i quiz con i preferiti
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun refreshQuizzes(userId: Int?) {
        viewModelScope.launch {
            val quizzes = if (userId != null) {
                repository.getQuizzesWithFavorite(userId).first()
            } else {
                repository.getAllQuizzes().first().map { quiz ->
                    QuizWithFavorite(
                        id = quiz.id,
                        name = quiz.name,
                        description = quiz.description,
                        imageUri = quiz.imageUri,
                        isComplete = quiz.isComplete,
                        isFavorite = false
                    )
                }
            }
            _state.value = QuizState(quizzes)
        }
    }


    fun toggleFavorite(userId: Int, quiz: Quiz) {
        viewModelScope.launch {
            val fav = repository.getFavorite(userId, quiz.id)
            if (fav == null) {
                repository.insertFavorite(FavoriteQuiz(personId = userId, quizId = quiz.id))
            } else {
                repository.deleteFavorite(fav)
            }
            // aggiorna la UI
            refreshQuizzes(userId)
        }
    }
}
