package com.example.quizapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.repositories.QuizRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuizState(val quizzes: List<Quiz>)

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    val state = repository.quizzes.map { QuizState(quizzes = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = QuizState(emptyList())
    )

}