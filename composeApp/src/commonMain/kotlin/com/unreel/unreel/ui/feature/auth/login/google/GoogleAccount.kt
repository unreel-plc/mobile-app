package com.unreel.unreel.ui.feature.auth.login.google

import androidx.compose.runtime.Composable

data class GoogleAccount(
    val token: String,
    val displayName: String = "",
    val profileImageUrl: String? = null
)

expect class GoogleAuthUiProvider {
    suspend fun signIn(): GoogleAccount?
}

expect class GoogleAuthProvider {
    @Composable
    fun getUiProvider(): GoogleAuthUiProvider

    suspend fun signOut()
}