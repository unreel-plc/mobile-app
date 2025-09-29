package com.unreel.core.activies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.repository.RemoteRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.context.GlobalContext

class ShareReceiverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)

            if (!sharedText.isNullOrEmpty()) {
                val work = OneTimeWorkRequestBuilder<ShareWorker>()
                    .setInputData(
                        workDataOf("shared_text" to sharedText)
                    )
                    .build()

                WorkManager.getInstance(this).enqueue(work)
            }
        }

        finish()
    }
}

class ShareWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {


    private val offlineRepository: OfflineRepository = GlobalContext.get().get()
    private val remoteRepository: RemoteRepository = GlobalContext.get().get()

    override suspend fun doWork(): Result {
        val sharedText = inputData.getString("shared_text") ?: return Result.failure()

        return try {
            val token = offlineRepository.getAccessToken().firstOrNull()

            if (!token.isNullOrEmpty()) {
                remoteRepository.detect(sharedText)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}