package com.example.quizapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.quizapp.data.models.QuizWithFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDAO {
    @Query("SELECT * FROM Quiz ORDER BY name ASC")
    fun getAll(): Flow<List<Quiz>>

    @Query("SELECT COUNT(*) FROM Quiz")
    suspend fun getQuizzesCount(): Int

    @Update
    suspend fun updateQuiz(quiz: Quiz)

    @Upsert
    suspend fun upsert(quiz: Quiz)

    @Delete
    suspend fun delete(quiz: Quiz)

    @Query("SELECT * FROM Quiz WHERE id = :id LIMIT 1")
    suspend fun getQuiz(id: Int): Quiz

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

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteQuiz WHERE personId = :personId AND quizId = :quizId)")
    suspend fun exists(personId: Int, quizId: Int): Boolean

    @Query("DELETE FROM FavoriteQuiz WHERE personId = :personId AND quizId = :quizId")
    suspend fun deleteByPersonAndQuiz(personId: Int, quizId: Int)

    @Query("""
    SELECT q.id, q.name, q.description, q.imageUri, q.isComplete,
           CASE WHEN f.id IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
    FROM Quiz q
    LEFT JOIN FavoriteQuiz f 
      ON q.id = f.quizId AND (:personId IS NOT NULL AND f.personId = :personId)
""")
    fun getQuizzesWithFavorite(personId: Int?): Flow<List<QuizWithFavorite>>

    @Query("SELECT * FROM Quiz")
    fun getAllQuizzes(): Flow<List<Quiz>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(fav: FavoriteQuiz)

    @Delete
    suspend fun deleteFavorite(fav: FavoriteQuiz)

    @Query("SELECT * FROM FavoriteQuiz WHERE personId = :userId AND quizId = :quizId LIMIT 1")
    suspend fun getFavorite(userId: Int, quizId: Int): FavoriteQuiz?

    @Transaction
    suspend fun populateSampleData() {
        // Quiz 1 - Matematica
        val quizId1 = insertQuiz(
            Quiz(
                name = "Basic Math",
                description = "Quiz for beginners",
                imageUri = "android.resource://com.example.quizapp/drawable/math_quiz",
                isComplete = true
            )
        ).toInt()

        // Domanda 1
        val questionId1 = insertQuestion(
            Question(
                questionText = "How much is 2+2?",
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
                questionText = "How much is 3×5?",
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
                questionText = "How much is 12 ÷ 4?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "2", questionId = questionId3, isCorrect = false))
        insertAnswer(Answer(answerText = "3", questionId = questionId3, isCorrect = true))
        insertAnswer(Answer(answerText = "4", questionId = questionId3, isCorrect = false))

// Domanda 4
        val questionId4 = insertQuestion(
            Question(
                questionText = "What is the square root of 64?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "6", questionId = questionId4, isCorrect = false))
        insertAnswer(Answer(answerText = "8", questionId = questionId4, isCorrect = true))
        insertAnswer(Answer(answerText = "10", questionId = questionId4, isCorrect = false))

// Domanda 5
        val questionId5 = insertQuestion(
            Question(
                questionText = "How much is 7²?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "14", questionId = questionId5, isCorrect = false))
        insertAnswer(Answer(answerText = "49", questionId = questionId5, isCorrect = true))
        insertAnswer(Answer(answerText = "21", questionId = questionId5, isCorrect = false))

// Domanda 6
        val questionId6 = insertQuestion(
            Question(
                questionText = "What is 50% of 100?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "25", questionId = questionId6, isCorrect = false))
        insertAnswer(Answer(answerText = "50", questionId = questionId6, isCorrect = true))
        insertAnswer(Answer(answerText = "75", questionId = questionId6, isCorrect = false))

// Domanda 7
        val questionId7 = insertQuestion(
            Question(
                questionText = "How much is 9 - 5 + 3?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "7", questionId = questionId7, isCorrect = true))
        insertAnswer(Answer(answerText = "6", questionId = questionId7, isCorrect = false))
        insertAnswer(Answer(answerText = "8", questionId = questionId7, isCorrect = false))

// Domanda 8
        val questionId8 = insertQuestion(
            Question(
                questionText = "How much is 4 × (3 + 2)?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "14", questionId = questionId8, isCorrect = false))
        insertAnswer(Answer(answerText = "20", questionId = questionId8, isCorrect = true))
        insertAnswer(Answer(answerText = "24", questionId = questionId8, isCorrect = false))

// Domanda 9
        val questionId9 = insertQuestion(
            Question(
                questionText = "How much is 18 ÷ 3 × 2?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "6", questionId = questionId9, isCorrect = false))
        insertAnswer(Answer(answerText = "12", questionId = questionId9, isCorrect = true))
        insertAnswer(Answer(answerText = "9", questionId = questionId9, isCorrect = false))

// Domanda 10
        val questionId10 = insertQuestion(
            Question(
                questionText = "Which is the prime number among these?",
                quizId = quizId1
            )
        ).toInt()

        insertAnswer(Answer(answerText = "15", questionId = questionId10, isCorrect = false))
        insertAnswer(Answer(answerText = "21", questionId = questionId10, isCorrect = false))
        insertAnswer(Answer(answerText = "17", questionId = questionId10, isCorrect = true))

        // Quiz 2 - Storia
        val quizId2 = insertQuiz(
            Quiz(
                name = "Ancient History",
                description = "Quiz about ancient Rome",
                imageUri = "android.resource://com.example.quizapp/drawable/storia",
                isComplete = false
            )
        ).toInt()

        // Domanda 1
        val questionId11 = insertQuestion(
            Question(
                questionText = "Who was the first Roman emperor?",
                quizId = quizId2
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Giulio Cesare", questionId =  questionId11, isCorrect =  false))
        insertAnswer(Answer(answerText = "Augusto", questionId =  questionId11, isCorrect =  true))
        insertAnswer(Answer(answerText = "Nerone", questionId =  questionId11, isCorrect =  false))

        //Domanda 2
        val questionId12 = insertQuestion(
            Question(
                questionText = "When the Roman Empire Collapsed?",
                quizId = quizId2
            )
        ).toInt()

        insertAnswer(Answer(answerText = "476", questionId = questionId12, isCorrect = true))
        insertAnswer(Answer(answerText = "465", questionId = questionId12, isCorrect = false))
        insertAnswer(Answer(answerText = "456", questionId = questionId12, isCorrect = false))

        val quizId3 = insertQuiz(
                Quiz(
                    name = "Geography",
                    description = "Capitals and countries of the world",
                    imageUri = "android.resource://com.example.quizapp/drawable/geography_quiz",
                    isComplete = false
                )
                ).toInt()

        val questionId13 = insertQuestion(
            Question(
                questionText = "What is the capital of France?",
                quizId = quizId3
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Paris", questionId = questionId13, isCorrect = true))
        insertAnswer(Answer(answerText = "Lyon", questionId = questionId13, isCorrect = false))
        insertAnswer(Answer(answerText = "Marseille", questionId = questionId13, isCorrect = false))

        val q32 = insertQuestion(
            Question(
                questionText = "Which river flows through Egypt?", quizId = quizId3)).toInt()
        insertAnswer(Answer(answerText = "Nile",questionId = q32, isCorrect = true))
        insertAnswer(Answer(answerText = "Amazon River", questionId = q32, isCorrect = false))
        insertAnswer(Answer(answerText = "Danube", questionId = q32, isCorrect = false))

        val q33 = insertQuestion(Question(questionText = "How many states make up the USA?", quizId = quizId3)).toInt()
        insertAnswer(Answer(answerText ="48", questionId =q33, isCorrect =false))
        insertAnswer(Answer(answerText ="50", questionId =q33, isCorrect =true))
        insertAnswer(Answer(answerText ="52", questionId =q33, isCorrect =false))


// Quiz 4 - Scienze
        val quizId4 = insertQuiz(
            Quiz(
                name = "Science",
                description = "Natural Science Quiz",
                imageUri = "android.resource://com.example.quizapp/drawable/science_quiz",
                isComplete = false
            )
        ).toInt()

        val questionId14 = insertQuestion(
            Question(
                questionText = "What is the closest planet to the Sun?",
                quizId = quizId4
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Mercury", questionId = questionId14, isCorrect = true))
        insertAnswer(Answer(answerText = "Venus", questionId = questionId14, isCorrect = false))
        insertAnswer(Answer(answerText = "Mars", questionId = questionId14, isCorrect = false))

        val q42 = insertQuestion(Question(questionText = "What is the chemical formula of water?", quizId =quizId4)).toInt()
        insertAnswer(Answer(answerText ="CO2", questionId =q42, isCorrect =false))
        insertAnswer(Answer(answerText ="H2O", questionId =q42, isCorrect =true))
        insertAnswer(Answer(answerText ="O2", questionId =q42, isCorrect =false))

        val q43 = insertQuestion(Question(questionText = "Who proposed the theory of evolution?", quizId =quizId4)).toInt()
        insertAnswer(Answer(answerText ="Einstein", questionId =q43, isCorrect =false))
        insertAnswer(Answer(answerText ="Darwin", questionId =q43, isCorrect =true))
        insertAnswer(Answer(answerText ="Newton", questionId =q43, isCorrect =false))


// Quiz 5 - Informatica
        val quizId5 = insertQuiz(
            Quiz(
                name = "Informatics",
                description = "Quiz on the world of computers",
                imageUri = "content://cs_quiz.jpg",
                isComplete = false
            )
        ).toInt()

        val questionId15 = insertQuestion(
            Question(
                questionText = "Who invented the World Wide Web?",
                quizId = quizId5
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Tim Berners-Lee", questionId = questionId15, isCorrect = true))
        insertAnswer(Answer(answerText = "Bill Gates", questionId = questionId15, isCorrect = false))
        insertAnswer(Answer(answerText = "Steve Jobs", questionId = questionId15, isCorrect = false))

        val q61 = insertQuestion(Question(questionText ="Who is considered the father of the computer?",quizId = quizId5)).toInt()
        insertAnswer(Answer(answerText ="Charles Babbage", questionId =q61, isCorrect = true))
        insertAnswer(Answer(answerText ="Alan Turing", questionId =q61, isCorrect =false))
        insertAnswer(Answer(answerText ="Bill Gates", questionId =q61, isCorrect =false))

        val q62 = insertQuestion(Question(questionText ="What does HTML mean?", quizId = quizId5)).toInt()
        insertAnswer(Answer(answerText ="HyperText Markup Language", questionId =q62, isCorrect = true))
        insertAnswer(Answer(answerText ="High Tech Modern Language", questionId =q62, isCorrect =false))
        insertAnswer(Answer(answerText ="Home Tool Machine Language", questionId =q62, isCorrect =false))

        val q63 = insertQuestion(Question(questionText ="Which company created Android?", quizId = quizId5)).toInt()
        insertAnswer(Answer(answerText ="Apple", questionId =q63, isCorrect =false))
        insertAnswer(Answer(answerText ="Google", questionId =q63, isCorrect =true))
        insertAnswer(Answer(answerText ="Microsoft", questionId =q63, isCorrect =false))


// Quiz 6 - Sport
        val quizId6 = insertQuiz(
            Quiz(
                name = "Sport",
                description = "Sports quiz",
                imageUri = "content://sport_quiz.jpg",
                isComplete = false
            )
        ).toInt()

        val questionId16 = insertQuestion(
            Question(
                questionText = "How many players does a soccer team have on the field?",
                quizId = quizId6
            )
        ).toInt()

        insertAnswer(Answer(answerText = "9", questionId = questionId16, isCorrect = false))
        insertAnswer(Answer(answerText = "10", questionId = questionId16, isCorrect = false))
        insertAnswer(Answer(answerText = "11", questionId = questionId16, isCorrect = true))

        val q51 = insertQuestion(Question(questionText ="In which sport is the oval ball used?", quizId =quizId6)).toInt()
        insertAnswer(Answer(answerText ="Rugby", questionId =q51, isCorrect =true))
        insertAnswer(Answer(answerText ="Soccer", questionId =q51,isCorrect = false))
        insertAnswer(Answer(answerText ="Basket", questionId =q51, isCorrect =false))
    }
}