package com.unreel.common

expect class ImagePicker {
    suspend fun pickImage(): PlatformFile?
}