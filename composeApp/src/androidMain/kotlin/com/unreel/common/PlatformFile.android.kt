package com.unreel.common
import java.io.File

actual class PlatformFile(private val file: File) {
    actual val name: String = file.name
    actual val path: String = file.absolutePath

    actual suspend fun readBytes(): ByteArray = file.readBytes()
}

actual suspend fun PlatformFile.toByteArray(): ByteArray = readBytes()

fun File.toPlatformFile(): PlatformFile = PlatformFile(this)