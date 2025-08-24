package com.example.quizapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
        LIMIT 3
    """)
    suspend fun getBestScoresForPerson(personId: Int): List<Score>

    @Delete
    suspend fun deletePerson(person: Person)

    @Insert
    suspend fun insertAnswer(answer: Answer): Long

    @Transaction
    @Query("SELECT * FROM Quiz WHERE id = :quizId")
    suspend fun getQuizWithQuestionsAndAnswers(quizId: Int): QuizWithQuestions

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: Badge): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPersonBadge(personBadge: PersonBadge)

    @Query("SELECT b.* FROM Badge b INNER JOIN PersonBadge pb ON b.id = pb.badgeId WHERE pb.personId = :personId")
    suspend fun getBadgesForPerson(personId: Int): List<Badge>

    @Query("SELECT * FROM Badge WHERE name = :name LIMIT 1")
    suspend fun getBadgeByName(name: String): Badge?

    @Query("SELECT * FROM Person WHERE id = :personId LIMIT 1")
    suspend fun getPersonById(personId: Int): Person?

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

        // Quiz 1 - Matematica (continuazione)
// Domanda 3
        val questionId3 = insertQuestion(
            Question(
                questionText = "Quanto fa 12 ÷ 4?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "2", questionId = questionId3, isCorrect = false))
        insertAnswer(Answer(answerText = "3", questionId = questionId3, isCorrect = true))
        insertAnswer(Answer(answerText = "4", questionId = questionId3, isCorrect = false))

// Domanda 4
        val questionId4 = insertQuestion(
            Question(
                questionText = "Qual è la radice quadrata di 64?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "6", questionId = questionId4, isCorrect = false))
        insertAnswer(Answer(answerText = "8", questionId = questionId4, isCorrect = true))
        insertAnswer(Answer(answerText = "10", questionId = questionId4, isCorrect = false))

// Domanda 5
        val questionId5 = insertQuestion(
            Question(
                questionText = "Quanto fa 7²?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "14", questionId = questionId5, isCorrect = false))
        insertAnswer(Answer(answerText = "49", questionId = questionId5, isCorrect = true))
        insertAnswer(Answer(answerText = "21", questionId = questionId5, isCorrect = false))

// Domanda 6
        val questionId6 = insertQuestion(
            Question(
                questionText = "Qual è il 50% di 100?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "25", questionId = questionId6, isCorrect = false))
        insertAnswer(Answer(answerText = "50", questionId = questionId6, isCorrect = true))
        insertAnswer(Answer(answerText = "75", questionId = questionId6, isCorrect = false))

// Domanda 7
        val questionId7 = insertQuestion(
            Question(
                questionText = "Quanto fa 9 - 5 + 3?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "7", questionId = questionId7, isCorrect = true))
        insertAnswer(Answer(answerText = "6", questionId = questionId7, isCorrect = false))
        insertAnswer(Answer(answerText = "8", questionId = questionId7, isCorrect = false))

// Domanda 8
        val questionId8 = insertQuestion(
            Question(
                questionText = "Qual è il risultato di 4 × (3 + 2)?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "14", questionId = questionId8, isCorrect = false))
        insertAnswer(Answer(answerText = "20", questionId = questionId8, isCorrect = true))
        insertAnswer(Answer(answerText = "24", questionId = questionId8, isCorrect = false))

// Domanda 9
        val questionId9 = insertQuestion(
            Question(
                questionText = "Quanto fa 18 ÷ 3 × 2?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "6", questionId = questionId9, isCorrect = false))
        insertAnswer(Answer(answerText = "12", questionId = questionId9, isCorrect = true))
        insertAnswer(Answer(answerText = "9", questionId = questionId9, isCorrect = false))

// Domanda 10
        val questionId10 = insertQuestion(
            Question(
                questionText = "Qual è il numero primo tra questi?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "15", questionId = questionId10, isCorrect = false))
        insertAnswer(Answer(answerText = "21", questionId = questionId10, isCorrect = false))
        insertAnswer(Answer(answerText = "17", questionId = questionId10, isCorrect = true))

        // Quiz 2 - Storia
        val quizId2 = insertQuiz(
            Quiz(
                name = "Storia Antica",
                description = "Quiz sull'antica Roma",
                imageUri = null,
                isComplete = false
            )
        ).toInt()

        // Domanda 1
        val questionId11 = insertQuestion(
            Question(
                questionText = "Chi fu il primo imperatore romano?",
                quizId = quizId2
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Giulio Cesare", questionId =  questionId11, isCorrect =  false))
        insertAnswer(Answer(answerText = "Augusto", questionId =  questionId11, isCorrect =  true))
        insertAnswer(Answer(answerText = "Nerone", questionId =  questionId11, isCorrect =  false))

        //Domanda 2
        val questionId12 = insertQuestion(
            Question(
                questionText = "Quando è crollato l'impero romano?",
                quizId = quizId2
            )
        ).toInt()

        insertAnswer(Answer(answerText = "476", questionId = questionId12, isCorrect = true))
        insertAnswer(Answer(answerText = "465", questionId = questionId12, isCorrect = false))
        insertAnswer(Answer(answerText = "456", questionId = questionId12, isCorrect = false))
    }
}