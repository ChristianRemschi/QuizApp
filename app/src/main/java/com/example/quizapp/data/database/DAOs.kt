package com.example.quizapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDAO {
    @Query("SELECT * FROM Quiz ORDER BY name ASC")
    fun getAll(): Flow<List<Quiz>>

    @Upsert
    suspend fun upsert(quiz: Quiz)

    @Delete
    suspend fun delete(quiz: Quiz)
}