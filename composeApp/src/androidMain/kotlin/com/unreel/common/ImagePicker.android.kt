package com.unreel.common

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume

actual class ImagePicker(private val context: Context) {
    actual suspend fun pickImage(): PlatformFile? {
        return suspendCancellableCoroutine { continuation ->
            // This will be handled by the composable function
            continuation.resume(null)
        }
    }
}

@Composable
fun rememberImagePicker(onImagePicked: (PlatformFile?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val file = uriToFile(context, uri)
            if (file != null) {
                onImagePicked(file.toPlatformFile())
            } else {
                onImagePicked(null)
            }
        } else {
            onImagePicked(null)
        }
    }

    return {
        launcher.launch("image/*")
    }
}

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream == null) {
            return null
        }

        val mimeType = context.contentResolver.getType(uri)

        val extension = when (mimeType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            "image/webp" -> ".webp"
            else -> ".jpg"
        }

        val tempFile = File(context.cacheDir, "profile_image_${System.currentTimeMillis()}$extension")

        val outputStream = FileOutputStream(tempFile)
        val bytesCopied = inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()


        if (tempFile.exists() && tempFile.length() > 0) {
            tempFile
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}