package com.example.quizapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quiz::class, Question::class, Answer::class, Score::class , Person::class, Badge::class, PersonBadge::class], version = 5)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun QuizDAO(): QuizDAO
}

