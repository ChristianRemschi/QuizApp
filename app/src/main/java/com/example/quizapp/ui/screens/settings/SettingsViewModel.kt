package com.example.quizapp.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.models.Theme
import com.example.quizapp.data.repositories.SettingsRepository
import com.example.quizapp.ui.screens.theme.ThemeScreen
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

data class SettingsState(val username: String)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState(""))
        private set

    fun setUsername(username: String) {
        state = SettingsState(username)
        viewModelScope.launch {
            repository.setUsername(username)
        }
    }

    init {
        viewModelScope.launch {
            state = SettingsState(repository.username.first())
        }
    }

}