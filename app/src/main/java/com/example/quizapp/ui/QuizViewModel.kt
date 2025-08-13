package com.example.quizapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.QuizWithQuestions
import com.example.quizapp.data.repositories.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuizState(val quizzes: List<Quiz>)

interface QuizActions {
    fun addQuiz(quiz: Quiz): Job
    fun removeQuiz(quiz: Quiz): Job
    fun toggleComplete(quiz: Quiz): Job
}

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    val state = repository.quizzes.map { QuizState(quizzes = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = QuizState(emptyList())
    )

    val actions = object : QuizActions {
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
        }
    }

}
