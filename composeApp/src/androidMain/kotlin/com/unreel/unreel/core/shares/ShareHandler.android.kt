package com.unreel.unreel.core.shares

import android.app.Activity
import android.content.Intent
import android.net.Uri

actual class ShareHandler(private val activity: Activity) {
    actual fun handleSharedContent(onReceived: (SharedData) -> Unit) {
        val intent = activity.intent
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                    onReceived(SharedData(type = "text", text = sharedText))
                } else if (intent.type?.startsWith("image/") == true) {
                    val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                    onReceived(
                        SharedData(type = "image", uris = listOf(uri.toString()))
                    )
                }
            }

            Intent.ACTION_SEND_MULTIPLE -> {
                if (intent.type?.startsWith("image/") == true) {
                    val uris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                    onReceived(
                        SharedData(
                            type = "images",
                            uris = uris?.map { it.toString() } ?: emptyList()
                        )
                    )
                }
            }
        }
    }
}