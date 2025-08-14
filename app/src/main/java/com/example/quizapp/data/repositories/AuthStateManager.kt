package com.example.quizapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthStateManager(private val userPreferences: DataStore<Preferences>) {
    val currentUserId: Flow<Int?> = userPreferences.data
        .map { preferences ->
            preferences[USER_ID_KEY]?.toIntOrNull()
        }

    suspend fun setLoggedInUser(userId: Int) {
        userPreferences.edit { preferences ->
            preferences[USER_ID_KEY] = userId.toString()
        }
    }

    suspend fun logout() {
        userPreferences.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }

    companion object {

        val USER_ID_KEY = stringPreferencesKey("user_id")
    }
}