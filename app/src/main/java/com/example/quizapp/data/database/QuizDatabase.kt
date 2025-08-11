package com.example.quizapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quiz::class], version = 2)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun QuizDAO(): QuizDAO
}
