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

    @Query("SELECT * FROM Quiz WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<Quiz>>

    @Update
    suspend fun updateQuiz(quiz: Quiz)

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
                imageUri = "android.resource://com.example.quizapp/drawable/math_quiz",
                isComplete = true,
                isFavorite = false
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
                imageUri = "android.resource://com.example.quizapp/drawable/storia",
                isComplete = false,
                isFavorite = false
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

        val quizId3 = insertQuiz(
                Quiz(
                    name = "Geografia",
                    description = "Capitali e paesi del mondo",
                    imageUri = "android.resource://com.example.quizapp/drawable/geography_quiz",
                    isComplete = false,
                    isFavorite = false
                )
                ).toInt()

        val questionId13 = insertQuestion(
            Question(
                questionText = "Qual è la capitale della Francia?",
                quizId = quizId3
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Parigi", questionId = questionId13, isCorrect = true))
        insertAnswer(Answer(answerText = "Lione", questionId = questionId13, isCorrect = false))
        insertAnswer(Answer(answerText = "Marsiglia", questionId = questionId13, isCorrect = false))

        val q32 = insertQuestion(
            Question(
                questionText = "Quale fiume attraversa l'Egitto?", quizId = quizId3)).toInt()
        insertAnswer(Answer(answerText = "Nilo",questionId = q32, isCorrect = true))
        insertAnswer(Answer(answerText = "Amazonas", questionId = q32, isCorrect = false))
        insertAnswer(Answer(answerText = "Danubio", questionId = q32, isCorrect = false))

        val q33 = insertQuestion(Question(questionText = "Quanti stati compongono gli USA?", quizId = quizId3)).toInt()
        insertAnswer(Answer(answerText ="48", questionId =q33, isCorrect =false))
        insertAnswer(Answer(answerText ="50", questionId =q33, isCorrect =true))
        insertAnswer(Answer(answerText ="52", questionId =q33, isCorrect =false))


// Quiz 4 - Scienze
        val quizId4 = insertQuiz(
            Quiz(
                name = "Scienze",
                description = "Quiz di scienze naturali",
                imageUri = "android.resource://com.example.quizapp/drawable/science_quiz",
                isComplete = false,
                isFavorite = false
            )
        ).toInt()

        val questionId14 = insertQuestion(
            Question(
                questionText = "Qual è il pianeta più vicino al Sole?",
                quizId = quizId4
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Mercurio", questionId = questionId14, isCorrect = true))
        insertAnswer(Answer(answerText = "Venere", questionId = questionId14, isCorrect = false))
        insertAnswer(Answer(answerText = "Marte", questionId = questionId14, isCorrect = false))

        val q42 = insertQuestion(Question(questionText = "Qual è la formula chimica dell’acqua?", quizId =quizId4)).toInt()
        insertAnswer(Answer(answerText ="CO2", questionId =q42, isCorrect =false))
        insertAnswer(Answer(answerText ="H2O", questionId =q42, isCorrect =true))
        insertAnswer(Answer(answerText ="O2", questionId =q42, isCorrect =false))

        val q43 = insertQuestion(Question(questionText = "Chi propose la teoria dell’evoluzione?", quizId =quizId4)).toInt()
        insertAnswer(Answer(answerText ="Einstein", questionId =q43, isCorrect =false))
        insertAnswer(Answer(answerText ="Darwin", questionId =q43, isCorrect =true))
        insertAnswer(Answer(answerText ="Newton", questionId =q43, isCorrect =false))


// Quiz 5 - Informatica
        val quizId5 = insertQuiz(
            Quiz(
                name = "Informatica",
                description = "Quiz sul mondo dei computer",
                imageUri = "content://cs_quiz.jpg",
                isComplete = false,
                isFavorite = false
            )
        ).toInt()

        val questionId15 = insertQuestion(
            Question(
                questionText = "Chi ha inventato il World Wide Web?",
                quizId = quizId5
            )
        ).toInt()

        insertAnswer(Answer(answerText = "Tim Berners-Lee", questionId = questionId15, isCorrect = true))
        insertAnswer(Answer(answerText = "Bill Gates", questionId = questionId15, isCorrect = false))
        insertAnswer(Answer(answerText = "Steve Jobs", questionId = questionId15, isCorrect = false))

        val q61 = insertQuestion(Question(questionText ="Chi è considerato il padre del computer?",quizId = quizId5)).toInt()
        insertAnswer(Answer(answerText ="Charles Babbage", questionId =q61, isCorrect = true))
        insertAnswer(Answer(answerText ="Alan Turing", questionId =q61, isCorrect =false))
        insertAnswer(Answer(answerText ="Bill Gates", questionId =q61, isCorrect =false))

        val q62 = insertQuestion(Question(questionText ="Cosa significa HTML?", quizId = quizId5)).toInt()
        insertAnswer(Answer(answerText ="HyperText Markup Language", questionId =q62, isCorrect = true))
        insertAnswer(Answer(answerText ="High Tech Modern Language", questionId =q62, isCorrect =false))
        insertAnswer(Answer(answerText ="Home Tool Machine Language", questionId =q62, isCorrect =false))

        val q63 = insertQuestion(Question(questionText ="Quale azienda ha creato Android?", quizId = quizId5)).toInt()
        insertAnswer(Answer(answerText ="Apple", questionId =q63, isCorrect =false))
        insertAnswer(Answer(answerText ="Google", questionId =q63, isCorrect =true))
        insertAnswer(Answer(answerText ="Microsoft", questionId =q63, isCorrect =false))


// Quiz 6 - Sport
        val quizId6 = insertQuiz(
            Quiz(
                name = "Sport",
                description = "Quiz sullo sport",
                imageUri = "content://sport_quiz.jpg",
                isComplete = false,
                isFavorite = false
            )
        ).toInt()

        val questionId16 = insertQuestion(
            Question(
                questionText = "Quanti giocatori ha una squadra di calcio in campo?",
                quizId = quizId6
            )
        ).toInt()

        insertAnswer(Answer(answerText = "9", questionId = questionId16, isCorrect = false))
        insertAnswer(Answer(answerText = "10", questionId = questionId16, isCorrect = false))
        insertAnswer(Answer(answerText = "11", questionId = questionId16, isCorrect = true))

        val q51 = insertQuestion(Question(questionText ="In che sport si usa la palla ovale?", quizId =quizId6)).toInt()
        insertAnswer(Answer(answerText ="Rugby", questionId =q51, isCorrect =true))
        insertAnswer(Answer(answerText ="Calcio", questionId =q51,isCorrect = false))
        insertAnswer(Answer(answerText ="Basket", questionId =q51, isCorrect =false))
    }
}