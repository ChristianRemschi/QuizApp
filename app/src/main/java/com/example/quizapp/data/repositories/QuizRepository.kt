package com.example.quizapp.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.QuizDAO
import com.example.quizapp.utils.saveImageToStorage
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val dao: QuizDAO,
    private val contentResolver: ContentResolver
) {
    val quizzes: Flow<List<Quiz>> = dao.getAll()

    }