package com.unreel.common
expect class PlatformFile {
    val name: String
    val path: String
    suspend fun readBytes(): ByteArray
}

expect suspend fun PlatformFile.toByteArray(): ByteArray