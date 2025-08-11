package com.example.quizapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    val username = dataStore.data.map { it[USERNAME_KEY] ?: "" }

    suspend fun setUsername(username: String) = dataStore.edit { it[USERNAME_KEY] = username }
}