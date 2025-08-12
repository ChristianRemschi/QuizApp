package com.example.quizapp.ui.screens.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.quizapp.data.models.Theme
import com.example.quizapp.ui.composables.AppBar
import com.example.quizapp.ui.screens.theme.ThemeScreen
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    state: SettingsState,
    onUsernameChanged: (String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = { AppBar(navController, title = "Settings") }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(contentPadding).padding(12.dp).fillMaxSize()
        ) {OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChanged,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(36.dp))
            Text(
                text = state.username,
                style = MaterialTheme.typography.bodyLarge
            )
        }}}