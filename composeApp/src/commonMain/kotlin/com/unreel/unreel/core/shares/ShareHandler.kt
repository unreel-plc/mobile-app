package com.unreel.unreel.core.shares

expect class ShareHandler {
    fun handleSharedContent(onReceived: (SharedData) -> Unit)
}

data class SharedData(
    val type: String,
    val text: String? = null,
    val uris: List<String> = emptyList()
)