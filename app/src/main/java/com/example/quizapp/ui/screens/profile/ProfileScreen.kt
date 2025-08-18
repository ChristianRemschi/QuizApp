package com.example.quizapp.ui.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quizapp.R
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.data.database.Score
import com.example.quizapp.ui.QuizRoute
import com.example.quizapp.ui.composables.AppBar
import com.example.quizapp.utils.PermissionStatus
import com.example.quizapp.utils.rememberCameraLauncher
import com.example.quizapp.utils.rememberMultiplePermissions

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    userId: Int
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
    Scaffold(
        topBar = { AppBar(navController, title = "Profile") }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
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
                ViewProfileView(
                    name = user?.name ?: "",
                    bio = user?.biography,
                    photoUri = user?.photo,
                    scores = scores,
                    onEditClick = { isEditing = true },
                    onQuizClick = { quizId ->
                        navController.navigate(QuizRoute.QuizDetails(quizId))
                    },
                    onLogoutClick = { viewModel.logout() }
                )
            }
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
    onQuizClick: (Int) -> Unit,
    onLogoutClick: () -> Unit
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
                    Text("Edit Profile")
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
            text = "Your Scores:",
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
        Button(onClick = onLogoutClick){
            Text("Logout")
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
    val ctx = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                onImageSelected(uri)
            }
        }
    )

    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = { uri ->
            capturedImageUri = uri
            onImageSelected(uri)
        })

    val permissionHandler = rememberMultiplePermissions(
        listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> { cameraLauncher.captureImage() }
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } -> showPermissionDialog = true
        }
    }

//    fun getCurrentCamera() = rememberCoroutineScope().launch {
//        if (permissionHandler.statuses.none { it.value.isGranted }) {
//            permissionHandler.launchPermissionRequest()
//            return@launch
//        }
//    }


    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permessi necessari") },
            text = { Text("Per salvare la foto del profilo sono necessari i permessi di fotocamera e archiviazione") },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog = false
                    if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        permissionHandler.launchPermissionRequest()
                    }
                }) {
                    Text("Richiedi permessi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Annulla")
                }
            }
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Selezione immagine
        Box(contentAlignment = Alignment.Center) {
            ProfileImage(
                capturedImageUri?.toString() ?: selectedImageUri?.toString() ?: currentPhoto,
                size = 120.dp
            )

            IconButton(
                //onClick = cameraLauncher::captureImage,
                onClick = {
                    //if (permissionHandler.statuses.all { it.value.isGranted })
                    if (true) { //TODO forse permessi o metti la riga sopra
                        cameraLauncher.captureImage()
                    } else {
                        permissionHandler.launchPermissionRequest()
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Take a Picture")
            }
            // Pulsante galleria
            IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                Icon(Icons.Default.Image, contentDescription = "Select from Gallery")
            }
            if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .data(cameraLauncher.capturedImageUri)
                        .crossfade(true)
                        .build(),
                    "Captured Image"
                )
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

    if (!photoUri.isNullOrBlank()) {
        AsyncImage(
            model = photoUri,
            contentDescription = "Profile Picture",
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_default_profile),
            error = painterResource(R.drawable.ic_default_profile)
        )
    } else {
        Image(
            painter = painterResource(R.drawable.ic_default_profile),
            contentDescription = "Default Profile Photo",
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