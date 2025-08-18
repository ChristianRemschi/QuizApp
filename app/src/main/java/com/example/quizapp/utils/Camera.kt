package com.example.quizapp.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

interface CameraLauncher {
    val capturedImageUri: Uri
    fun captureImage()
}

@Composable
fun rememberCameraLauncher(
    onPictureTaken: (imageUri: Uri) -> Unit
): CameraLauncher {
    val ctx = LocalContext.current

    var imageUri by remember { mutableStateOf(Uri.EMPTY) }
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }

    val cameraActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
            if (!pictureTaken) return@rememberLauncherForActivityResult
            capturedImageUri = imageUri
            onPictureTaken(capturedImageUri)
        }

    val cameraLauncher = remember(cameraActivityLauncher) {
        object : CameraLauncher {
            override val capturedImageUri get() = capturedImageUri
            override fun captureImage() {
                val imageFile = File
                    .createTempFile("tmp_image", ".jpg", ctx.externalCacheDir)
                imageUri = FileProvider
                    .getUriForFile(ctx, ctx.packageName + ".provider", imageFile)
                cameraActivityLauncher.launch((imageUri))
            }
        }
    }
    return cameraLauncher
}