package com.example.quizapp.ui.screens.play

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.database.QuizDAO
import com.example.quizapp.data.database.QuizWithQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizapp.data.database.Score
import com.example.quizapp.data.repositories.QuizRepository
import kotlinx.coroutines.CoroutineScope

class PlayViewModel(private val quizDao: QuizDAO, private val quizRepository: QuizRepository) : ViewModel() {

    private val _quizData = MutableLiveData<QuizWithQuestions>()
    val quizData: LiveData<QuizWithQuestions> get() = _quizData

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = quizDao.getQuizWithQuestionsAndAnswers(quizId)
            _quizData.postValue(data)
        }
    }
    fun insertScore( personId: Int, quizId: Int, score: Int ){
        viewModelScope.launch(Dispatchers.IO) {
            val newScore = Score(
                personId = personId,
                quizId = quizId,
                score = score
            )
            quizDao.insertScore(newScore)
        }
    }
    fun assignBadge(userId: Int, badgeName: String, description: String, iconUri: String? = null, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            quizRepository.assignBadge(userId, badgeName, description, iconUri, scope, snackbarHostState)
        }
    }
}
