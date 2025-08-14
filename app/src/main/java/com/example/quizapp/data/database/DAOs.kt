package com.example.quizapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDAO {
    @Query("SELECT * FROM Quiz ORDER BY name ASC")
    fun getAll(): Flow<List<Quiz>>

    @Query("SELECT COUNT(*) FROM Quiz")
    suspend fun getQuizzesCount(): Int

    @Upsert
    suspend fun upsert(quiz: Quiz)

    @Delete
    suspend fun delete(quiz: Quiz)

    @Query("SELECT * FROM Quiz WHERE id = :id LIMIT 1")
    suspend fun getQuiz(id: Int): Quiz

//    @Query("SELECT * FROM Score WHERE personId = :id")
//    suspend fun getScoresByPerson(id: Int)

    @Insert
    suspend fun insertQuiz(quiz: Quiz): Long

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Insert
    suspend fun insertPerson(person: Person): Long

    @Query("SELECT * FROM Person WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Person?

    @Query("SELECT * FROM Person WHERE name = :username LIMIT 1")
    suspend fun getByUsername(username: String): Person?

    @Update
    suspend fun updatePerson(person: Person)

    @Insert
    suspend fun insertScore(score: Score): Long

    @Query("SELECT * FROM Score WHERE personId = :personId")
    suspend fun getScoresForPerson(personId: Int): List<Score>

    @Query("""
        SELECT * FROM Score
        WHERE personId = :personId
        ORDER BY score DESC
        LIMIT 1
    """)
    suspend fun getBestScoreForPerson(personId: Int): Score?

    @Delete
    suspend fun deletePerson(person: Person)

    @Insert
    suspend fun insertAnswer(answer: Answer): Long

    @Transaction
    @Query("SELECT * FROM Quiz WHERE id = :quizId")
    suspend fun getQuizWithQuestionsAndAnswers(quizId: Int): QuizWithQuestions

    @Transaction
    suspend fun populateSampleData() {
        // Quiz 1 - Matematica
        val quizId1 = insertQuiz(
            Quiz(
                name = "Matematica Base",
                description = "Quiz per principianti",
                imageUri = "content://math_quiz.jpg",
                isComplete = true
            )
        ).toInt()

        // Domanda 1
        val questionId1 = insertQuestion(
            Question(
                questionText = "Quanto fa 2+2?",
                quizId = quizId1
            )
        ).toInt()

        // Risposte per domanda 1
        insertAnswer(Answer(answerText = "3", questionId = questionId1, isCorrect = false))
        insertAnswer(Answer(answerText = "4", questionId = questionId1, isCorrect =  true))
        insertAnswer(Answer(answerText = "5", questionId = questionId1, isCorrect = false))

        // Domanda 2
        val questionId2 = insertQuestion(
            Question(
                questionText = "Quanto fa 3×5?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "10", questionId =  questionId2, isCorrect =  false))
        insertAnswer(Answer(answerText = "15", questionId =  questionId2, isCorrect =  true))
        insertAnswer(Answer(answerText = "20", questionId =  questionId2, isCorrect =  false))

        // Quiz 2 - Storia
        val quizId2 = insertQuiz(
            Quiz(
                name = "Storia Antica",
                description = "Quiz sull'antica Roma",
                imageUri = null,
                isComplete = false
            )
        ).toInt()

        // Domanda 3
        val questionId3 = insertQuestion(
            Question(
                questionText = "Chi fu il primo imperatore romano?",
                quizId = quizId2
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Giulio Cesare", questionId =  questionId3, isCorrect =  false))
        insertAnswer(Answer(answerText = "Augusto", questionId =  questionId3, isCorrect =  true))
        insertAnswer(Answer(answerText = "Nerone", questionId =  questionId3, isCorrect =  false))
    }
}