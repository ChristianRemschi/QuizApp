package com.example.quizapp.ui.screens.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.Score
import com.example.quizapp.ui.QuizRoute

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    userId: Int // Dovresti passare l'ID dell'utente loggato
) {
    // Carica i dati dell'utente all'avvio
    LaunchedEffect(Unit) {
        viewModel.loadUserData(userId)
    }

    val user by viewModel.userData.collectAsStateWithLifecycle()
    val scores by viewModel.userScores.collectAsStateWithLifecycle()

    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(user?.name ?: "") }
    var editedBio by remember { mutableStateOf(user?.biography ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isEditing) {
            // Modalità modifica
            EditProfileView(
                name = editedName,
                onNameChange = { editedName = it },
                bio = editedBio,
                onBioChange = { editedBio = it },
                currentPhoto = user?.photo,
                selectedImageUri = selectedImageUri,
                onImageSelected = { uri -> selectedImageUri = uri },
                onSave = {
                    viewModel.updateProfile(
                        editedName,
                        editedBio,
                        selectedImageUri?.toString() ?: user?.photo
                    )
                    isEditing = false
                },
                onCancel = { isEditing = false }
            )
        } else {
            // Visualizzazione normale
            ViewProfileView(
                name = user?.name ?: "",
                bio = user?.biography,
                photoUri = user?.photo,
                scores = scores,
                onEditClick = { isEditing = true },
                onQuizClick = { quizId ->
                    navController.navigate(QuizRoute.QuizDetails(quizId))
                }
            )
        }
    }
}

@Composable
private fun ViewProfileView(
    name: String,
    bio: String?,
    photoUri: String?,
    scores: List<Pair<Quiz, Score>>,
    onEditClick: () -> Unit,
    onQuizClick: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Foto profilo
            ProfileImage(photoUri, size = 80.dp)

            Spacer(modifier = Modifier.width(16.dp))

            // Nome utente e pulsante modifica
            Column {
                Text(text = name, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onEditClick) {
                    Text("Modifica Profilo")
                }
            }
        }

        // Biografia
        if (!bio.isNullOrBlank()) {
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Punteggi
        Text(
            text = "I tuoi punteggi:",
            style = MaterialTheme.typography.headlineSmall
        )

        if (scores.isEmpty()) {
            Text("Nessun quiz completato ancora!")
        } else {
            LazyColumn {
                items(scores) { (quiz, score) ->
                    QuizScoreItem(quiz, score, onQuizClick)
                }
            }
        }
    }
}

@Composable
private fun EditProfileView(
    name: String,
    onNameChange: (String) -> Unit,
    bio: String?,
    onBioChange: (String) -> Unit,
    currentPhoto: String?,
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Selezione immagine
        Box(contentAlignment = Alignment.Center) {
            ProfileImage(
                selectedImageUri?.toString() ?: currentPhoto,
                size = 120.dp
            )

            IconButton(
                onClick = { /* Apri la galleria o la fotocamera */ },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Cambia foto")
            }
        }

        // Campo nome
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo biografia
        OutlinedTextField(
            value = bio ?: "",
            onValueChange = onBioChange,
            label = { Text("Biografia") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        // Pulsanti salva/annulla
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onCancel) {
                Text("Annulla")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onSave) {
                Text("Salva")
            }
        }
    }
}

@Composable
private fun ProfileImage(photoUri: String?, size: Dp) {
    val imageModifier = Modifier
        .size(size)
        .clip(CircleShape)

    if (photoUri != null) {
        AsyncImage(
            model = photoUri,
            contentDescription = "Foto profilo",
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(R.drawable.ic_default_profile),
            contentDescription = "Foto profilo predefinita",
            modifier = imageModifier
        )
    }
}

@Composable
private fun QuizScoreItem(
    quiz: Quiz,
    score: Score,
    onQuizClick: (Int) -> Unit
) {
    Card(
        onClick = { onQuizClick(quiz.id) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Immagine del quiz (se presente)
            if (!quiz.imageUri.isNullOrBlank()) {
                AsyncImage(
                    model = quiz.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column {
                Text(
                    text = quiz.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Punteggio: ${score.score}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}