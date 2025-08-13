package com.example.quizapp.ui.screens.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.database.QuizDAO
import com.example.quizapp.data.database.QuizWithQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlayViewModel(private val quizDao: QuizDAO) : ViewModel() {

    private val _quizData = MutableLiveData<QuizWithQuestions>()
    val quizData: LiveData<QuizWithQuestions> get() = _quizData

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = quizDao.getQuizWithQuestionsAndAnswers(quizId)
            _quizData.postValue(data)
        }
    }
}
