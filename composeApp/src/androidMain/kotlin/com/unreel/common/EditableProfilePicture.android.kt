package com.unreel.common
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unreel.unreel.core.common.utils.Constants.DEFAULT_PROFILE_PICTURE
import coil3.compose.AsyncImage
import com.unreel.common.PlatformFile
import com.unreel.common.rememberImagePicker

@Composable
actual fun EditableProfilePicture(
    profilePicture: String?,
    onProfilePictureChanged: (PlatformFile?) -> Unit,
    height: Dp,
    shape: Shape,
    loading: Boolean,
    modifier: Modifier,
) {
    var selectedImageFile by remember { mutableStateOf<PlatformFile?>(null) }

    // Reset selected file when profilePicture changes
    LaunchedEffect(profilePicture) {
        if (selectedImageFile != null) {
            selectedImageFile = null
        }
    }

    val imagePickerLauncher = rememberImagePicker { file ->
        if (file != null) {
            selectedImageFile = file
            onProfilePictureChanged(file)
        } else {
            onProfilePictureChanged(null)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    imagePickerLauncher()
                }
                .size(height)
                .clip(shape)
                .border(2.dp, Color(0xFFF5F5F5), shape)
        ) {
            when {
                selectedImageFile != null -> {
                    AsyncImage(
                        model = "file://${selectedImageFile?.path}",
                        contentDescription = "Selected Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(height)
                            .clip(shape),
                        onLoading = {
                        },
                        onSuccess = {
                        },
                        onError = { error ->
                        }
                    )
                }
                profilePicture != null -> {
                    AsyncImage(
                        model = profilePicture,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(height)
                            .clip(shape),
                        onLoading = {
                        },
                        onSuccess = {
                        },
                        onError = { error ->
                        }
                    )
                }
                else -> {
                    AsyncImage(
                        model = DEFAULT_PROFILE_PICTURE,
                        contentDescription = "Default Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    )
                }
            }

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                }
            }
        }

        Button(
            onClick = {
                imagePickerLauncher()
            },
            enabled = !loading,
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = if (loading) "Uploading..." else "Choose photo")
        }
    }
}