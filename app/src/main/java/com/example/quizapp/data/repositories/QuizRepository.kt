package com.example.quizapp.data.repositories

import android.content.ContentResolver
import com.example.quizapp.data.database.Person
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.QuizDAO
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val dao: QuizDAO,
    private val contentResolver: ContentResolver
) {
    val quizzes: Flow<List<Quiz>> = dao.getAll()

    suspend fun upsert(quiz: Quiz) = dao.upsert(quiz)

    suspend fun delete(quiz: Quiz) = dao.delete(quiz)

    suspend fun populateSampleData() = dao.populateSampleData()

    suspend fun getQuizzesCount() = dao.getQuizzesCount()

    suspend fun insertPerson(person: Person) = dao.insertPerson(person)

    suspend fun getByUsername(username: String) = dao.getByUsername(username)

    }