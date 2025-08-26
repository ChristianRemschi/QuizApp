package com.example.quizapp.data.repositories

import android.util.Log
import com.example.quizapp.data.database.Badge
import com.example.quizapp.data.database.Person
import com.example.quizapp.data.database.PersonBadge
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.QuizDAO
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val dao: QuizDAO
) {
    val quizzes: Flow<List<Quiz>> = dao.getAll()

    suspend fun upsert(quiz: Quiz) = dao.upsert(quiz)

    suspend fun delete(quiz: Quiz) = dao.delete(quiz)

    suspend fun populateSampleData() = dao.populateSampleData()

    suspend fun getQuizzesCount() = dao.getQuizzesCount()

    suspend fun insertPerson(person: Person) = dao.insertPerson(person)

    suspend fun getByUsername(username: String) = dao.getByUsername(username)

    suspend fun getQuiz(id: Int) = dao.getQuiz(id)

    suspend fun getScoresForPerson(id: Int) = dao.getScoresForPerson(id)

    suspend fun getPersonById(id: Int) = dao.getById(id)

    suspend fun updatePerson(person: Person) = dao.updatePerson(person)

    suspend fun getBestScoresForPerson(personId: Int) = dao.getBestScoresForPerson(personId)

    suspend fun getBadgesForPerson(personId: Int) = dao.getBadgesForPerson(personId)

    suspend fun assignBadge(personId: Int, badgeName: String, description: String, iconUri: String? = null) {
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
    }

    }