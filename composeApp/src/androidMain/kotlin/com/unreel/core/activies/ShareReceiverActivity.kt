package com.unreel.core.activies

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.core.shares.ShareHandler
import com.unreel.unreel.networks.repository.RemoteRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class ShareReceiverActivity : ComponentActivity() {
    private val offlineRepository: OfflineRepository by inject()
    private val remoteRepository: RemoteRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = ShareHandler(this)

        handler.handleSharedContent { shared ->
            var token: String? = null;
            runBlocking {
                val token = offlineRepository.getAccessToken().firstOrNull()



                if (token.isNullOrEmpty() || shared.text.isNullOrEmpty()) {
                    return@runBlocking
                }

                remoteRepository.detect(shared.text)
            }

            finish()
        }
    }
}