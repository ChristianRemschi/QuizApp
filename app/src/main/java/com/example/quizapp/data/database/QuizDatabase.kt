package com.example.quizapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Quiz::class], version = 1)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun QuizDAO(): QuizDAO
}
