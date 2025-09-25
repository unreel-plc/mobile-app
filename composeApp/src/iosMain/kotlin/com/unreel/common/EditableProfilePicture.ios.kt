package com.unreel.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
actual fun EditableProfilePicture(
    profilePicture: String?,
    onProfilePictureChanged: (PlatformFile?) -> Unit,
    height: Dp,
    shape: Shape,
    loading: Boolean,
    modifier: Modifier
) {
}