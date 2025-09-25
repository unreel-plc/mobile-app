package com.unreel.common
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
expect fun EditableProfilePicture(
    profilePicture: String?,
    onProfilePictureChanged: (PlatformFile?) -> Unit,
    height: Dp = 100.dp,
    shape: Shape = CircleShape,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
)