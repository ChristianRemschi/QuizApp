package com.example.quizapp.data.repositories

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import com.example.quizapp.data.database.Badge
import com.example.quizapp.data.database.FavoriteQuiz
import com.example.quizapp.data.database.Person
import com.example.quizapp.data.database.PersonBadge
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.QuizDAO
import com.example.quizapp.data.models.QuizWithFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuizRepository(
    private val dao: QuizDAO
) {
    val quizzes: Flow<List<Quiz>> = dao.getAll()

    suspend fun upsert(quiz: Quiz) = dao.upsert(quiz)

    suspend fun delete(quiz: Quiz) = dao.delete(quiz)

    suspend fun populateSampleData() = dao.populateSampleData()

    suspend fun getQuizzesCount() = dao.getQuizzesCount()

    suspend fun insertPerson(person: Person) = dao.insertPerson(person)

    suspend fun getFavorite(userId: Int, quizId: Int) = dao.getFavorite(userId,quizId)

    suspend fun insertFavorite(fav: FavoriteQuiz) = dao.insertFavorite(fav)

    suspend fun deleteFavorite(fav: FavoriteQuiz) = dao.deleteFavorite(fav)

    suspend fun getQuizzesWithFavorite(personId: Int?) = dao.getQuizzesWithFavorite(personId)

    suspend fun getByUsername(username: String) = dao.getByUsername(username)

    suspend fun getQuiz(id: Int) = dao.getQuiz(id)

    suspend fun getScoresForPerson(id: Int) = dao.getScoresForPerson(id)

    suspend fun getPersonById(id: Int) = dao.getById(id)

    suspend fun updatePerson(person: Person) = dao.updatePerson(person)

    suspend fun getBestScoresForPerson(personId: Int) = dao.getBestScoresForPerson(personId)

    suspend fun getBadgesForPerson(personId: Int) = dao.getBadgesForPerson(personId)

    suspend fun assignBadge(personId: Int, badgeName: String, description: String, iconUri: String? = null, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
        val person = dao.getPersonById(personId)
        if (person == null) {
            Log.e("BADGE", "Person $personId non trovato!")
            return
        }
        if (dao.getBadgesForPerson(personId).contains(dao.getBadgeByName(badgeName))) {
            Log.e("BADGE_ALREADY_EXISTS", "Person $personId ha già il badge!")
            return
        }

        var badge = dao.getBadgeByName(badgeName)
        if (badge == null) {
            val badgeId = dao.insertBadge(Badge(name = badgeName, description = description, iconUri = iconUri))
            badge = Badge(id = badgeId.toInt(), name = badgeName, description = description, iconUri = iconUri)
        }
        dao.insertPersonBadge(PersonBadge(personId = personId, badgeId = badge.id))
        scope.launch {
            snackbarHostState.showSnackbar("You claimed a new badge: $badgeName")
        }
    }

    suspend fun isQuizFavorite(personId: Int, quizId: Int): Boolean {
        return dao.exists(personId, quizId)
    }

    suspend fun addFavorite(personId: Int, quizId: Int) {
        dao.insertFavorite(FavoriteQuiz(personId = personId, quizId = quizId))
    }

    suspend fun removeFavorite(personId: Int, quizId: Int) {
        dao.deleteByPersonAndQuiz(personId, quizId)
    }

    fun getQuizzesForUser(personId: Int?): Flow<List<QuizWithFavorite>> {
        return dao.getQuizzesWithFavorite(personId)
    }

    fun getAllQuizzes(): Flow<List<Quiz>> {
        return dao.getAllQuizzes()
    }

    suspend fun toggleFavorite(userId: Int, quiz: Quiz) {
        val fav = dao.getFavorite(userId, quiz.id)
        Log.d("prova2","prova2")
        if (fav == null) {
            dao.insertFavorite(FavoriteQuiz(personId = userId, quizId = quiz.id))
        } else {
            dao.deleteFavorite(fav)
        }
    }


    }