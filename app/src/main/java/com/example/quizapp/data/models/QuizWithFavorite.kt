package com.example.quizapp.data.models

data class QuizWithFavorite(
    val id: Int,
    val name: String,
    val description: String,
    val imageUri: String?,
    val isComplete: Boolean,
    val isFavorite: Boolean
)
