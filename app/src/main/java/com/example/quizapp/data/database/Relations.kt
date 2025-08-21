package com.example.quizapp.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswers(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val answers: List<Answer>
)

data class QuizWithQuestions(
    @Embedded val quiz: Quiz,
    @Relation(
        entity = Question::class,
        parentColumn = "id",
        entityColumn = "quizId"
    )
    val questions: List<QuestionWithAnswers>
) {
    fun getRandomQuestions(limit: Int = 5): List<QuestionWithAnswers> {
        return if (questions.size <= limit) {
            questions.shuffled()
        } else {
            questions.shuffled().take(limit)
        }
    }
}