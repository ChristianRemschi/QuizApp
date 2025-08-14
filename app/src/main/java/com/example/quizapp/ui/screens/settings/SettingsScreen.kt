package com.example.quizapp.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.ui.QuizRoute
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.composables.AppBar
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    state: SettingsState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    navController: NavController,
    quizViewModel: QuizViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { AppBar(navController, title = "Login") },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 32.dp)
                .fillMaxSize()
        ) {
            // Logo/Header (opzionale)
            Icon(
                Icons.Default.Lock,
                contentDescription = "Login",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Username Field
            OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChanged,
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    quizViewModel.login(state.username, state.password) { success ->
                        if (success) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Login con successo")
                                navController.navigate(QuizRoute.Home)
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Username o password errati")
                            }
                        }
                    } },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.username.isNotBlank() && state.password.isNotBlank()
            ) {
                Text("Login", style = MaterialTheme.typography.labelLarge)
            }
            //Register Button
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = {
                quizViewModel.createAccount(state.username, state.password) { success ->
                    if (success) {
                        onUsernameChanged("")
                        onPasswordChanged("")
                        scope.launch {
                            snackbarHostState.showSnackbar("Account creato con successo")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Username già esistente")
                        }
                    }
                }
                }) {
                Text("Create account")
            }
        }
    }
}