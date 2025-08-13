package com.example.quizapp.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SettingsState(
    val username: String,
    val password: String
)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState("", ""))
        private set

    fun setUsername(username: String) {
        state = state.copy(username = username)
        viewModelScope.launch {
            repository.setUsername(username)
        }
    }

    fun setPassword(password: String) {
        state = state.copy(password = password)
        viewModelScope.launch {
            repository.setPassword(password)
        }
    }

    fun login() {
        viewModelScope.launch {
            try {
                repository.login(state.username, state.password)
                // Navigate to home on success
            } catch (e: Exception) {
                // Handle error (show snackbar, etc.)
            }
        }
    }

    init {
        viewModelScope.launch {
            val username = repository.username.first()
            val password = repository.password.first()
            state = SettingsState(username, password)
        }
    }
}

