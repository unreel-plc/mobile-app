package com.unreel.unreel.ui.feature.auth.login.google

import androidx.compose.runtime.Composable

actual class GoogleAuthUiProvider {
    actual suspend fun signIn(): GoogleAccount? {
        TODO("Not yet implemented")
    }
}

actual class GoogleAuthProvider {
    @Composable
    actual fun getUiProvider(): GoogleAuthUiProvider {
        TODO("Not yet implemented")
    }

    actual suspend fun signOut() {
    }
}