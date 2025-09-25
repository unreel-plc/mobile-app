package com.unreel.di

import android.content.Context
import android.content.Intent
import com.unreel.unreel.DbClient
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.repository.IntentKeywords
import com.unreel.unreel.networks.repository.SessionManager
import com.unreel.unreel.MainActivity
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManager.Companion.create
import com.unreel.unreel.ui.feature.auth.login.google.GoogleAuthProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

actual val platformModule = module {
        singleOf(::DbClient)
        single<SessionManager> { AndroidSessionManager(get(), get()) }
}


class AndroidSessionManager(
        private val context: Context,
        private val offlineRepository: OfflineRepository
) : SessionManager {
        override suspend fun onSessionExpired() {
                offlineRepository.setAccessToken("")
                offlineRepository.setRefreshToken("")

                val intent = Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra(
                                IntentKeywords.StartDestination.name,
                                IntentKeywords.SESSION_EXPIRED.name
                        )
                }
                context.startActivity(intent)
        }
}


actual val loginPlatformModule: Module =
        module {
                factory { create(androidContext()) } bind CredentialManager::class
                factoryOf(::GoogleAuthProvider) bind GoogleAuthProvider::class
        }