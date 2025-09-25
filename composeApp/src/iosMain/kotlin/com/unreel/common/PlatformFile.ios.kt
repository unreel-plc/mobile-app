package com.unreel.common

actual class PlatformFile {
    actual val name: String
        get() = TODO("Not yet implemented")
    actual val path: String
        get() = TODO("Not yet implemented")

    actual suspend fun readBytes(): ByteArray {
        TODO("Not yet implemented")
    }
}

actual suspend fun PlatformFile.toByteArray(): ByteArray {
    TODO("Not yet implemented")
}