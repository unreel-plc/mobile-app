package com.unreel.unreel.networks.repository

import com.unreel.unreel.core.common.utils.Resource
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.ApiInterface
import com.unreel.unreel.networks.RefreshBody
import com.unreel.unreel.networks.models.BaseResponse
import com.unreel.unreel.networks.models.ErrorResponse
import com.unreel.unreel.networks.models.auth.DetectLinkResponse
import com.unreel.unreel.networks.models.auth.DetectResponse
import com.unreel.unreel.networks.models.auth.DownloadItem
import com.unreel.unreel.networks.models.auth.EngineDetectBody
import com.unreel.unreel.networks.models.auth.GetCategoriesResponse
import com.unreel.unreel.networks.models.auth.GetDownloadsResponse
import com.unreel.unreel.networks.models.auth.GetTagsResponse
import com.unreel.unreel.networks.models.auth.HelloResponse
import com.unreel.unreel.networks.models.auth.LoginBody
import com.unreel.unreel.networks.models.auth.LoginResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException

expect fun currentTimeMillis(): Long

interface SessionManager {
    suspend fun onSessionExpired()
}


class RemoteRepositoryImpl(
    private val api: ApiInterface,
    private val offlineRepository: OfflineRepository,
    private val sessionManager: SessionManager,
) : RemoteRepository {

    companion object {
        private val tokenRefreshMutex = Mutex()
        private var lastRefreshTime = 0L
        private const val REFRESH_COOLDOWN_MS = 1000L // Prevent multiple refreshes within 1 second
    }

    suspend fun onSessionExpired() {
        sessionManager.onSessionExpired()
        // to do
    }

    private suspend fun <T : BaseResponse> handleUnauthorizedError(
        invoke: suspend () -> T,
        retry: Int
    ): Resource<T> {
        return tokenRefreshMutex.withLock {
            val currentTime = currentTimeMillis()

            if (currentTime - lastRefreshTime < REFRESH_COOLDOWN_MS) {
                return@withLock requestInvoker<T>(1) { invoke() }
            }

            val refreshToken = offlineRepository.getRefreshToken().first()

            if (refreshToken != null && refreshToken != "") {
                val refreshResult = refreshToken(refreshToken)

                when (refreshResult) {
                    is Resource.Success -> {
//                        offlineRepository.setAccessToken(refreshResult.data?.accessToken!!)
//                        offlineRepository.setRefreshToken(refreshResult.data.refreshToken!!)
                        lastRefreshTime = currentTime

                        return@withLock requestInvoker<T>(retry + 1) { invoke() }
                    }

                    is Resource.Error -> {
                        onSessionExpired()
                        return@withLock Resource.Error("Session Expired")
                    }
                }
            } else {
                onSessionExpired()
                return@withLock Resource.Error("Session Expired")
            }
        }
    }

    protected suspend fun getAccessToken(): String {
        return "Bearer ${offlineRepository.getAccessToken().first()!!}"
    }

    protected suspend fun <T : BaseResponse> requestInvoker(
        retry: Int = 0,
        invoke: suspend () -> T
    ): Resource<T> {
        try {

            val response = invoke()
            return Resource.Success(response)
            /*println("REQUEST_INVOKER: Response received - Status: ${response.statusCode}, Message: ${response.message}")

            // Check if this is an error response
            if (response.statusCode >= 400) {
                println("REQUEST_INVOKER: Server returned error status: ${response.statusCode}")
                return Resource.Error(response.message, statusCode = response.statusCode)
            }

            return when (response.statusCode) {
                200, 201 -> Resource.Success(response)
                else -> {
                    println("REQUEST_INVOKER: Non-success status code: ${response.statusCode}, Message: ${response.message}")
                    Resource.Error(response.message, statusCode = response.statusCode)
                }
            }*/
        } catch (t: Throwable) {
            var errorMsg = ""
            var statusCode: Int? = null

            when (t) {
                is CancellationException -> {
                    throw t
                }

                is kotlinx.io.IOException -> {
                    errorMsg = "Network error: Unable to connect to server"
                }

                is ClientRequestException -> {
                    statusCode = t.response.status.value
                    errorMsg = parseErrorMessage(t.response)

                    if (statusCode == 401 && retry == 0) {
                        return handleUnauthorizedError(invoke, retry)
                    }
                }

                is ServerResponseException -> {
                    statusCode = t.response.status.value
                    errorMsg = parseErrorMessage(t.response)
                }

                is JsonConvertException -> {
                    // Handle JSON parsing errors specifically
                    errorMsg = "Failed to parse server response: ${t.message}"
                    // Try to get the raw response to see what the server actually sent
                    try {
                        if (t is ClientRequestException || t is ServerResponseException) {
                            val rawResponse = t.response.bodyAsText()
                        }
                    } catch (e: Exception) {
                    }
                }

                else -> {
                    errorMsg = "Unexpected error: ${t.message}"
                }
            }
            return Resource.Error(errorMsg, statusCode = statusCode)
        }
    }

    private suspend fun parseErrorMessage(response: HttpResponse): String {
        return try {
            val rawResponse = response.bodyAsText()

            val errorResponse = response.body<ErrorResponse>()
            errorResponse.toString()
//            errorResponse.message ?: "Unknown error"
        } catch (e: Exception) {
            "Something Went Wrong"
        }
    }

    suspend fun refreshToken(refreshToken: String): Resource<LoginResponse> {
        return requestInvoker(1) {
            api.refresh(RefreshBody(refreshToken))
        }
    }


    override suspend fun login(
        token: String,
    ): Resource<LoginResponse> {
        val loginBody = LoginBody(
            identifier = token,
        )
        return requestInvoker {
            api.login(loginBody)
        }
    }

    override suspend fun sendHello(
    ): Resource<HelloResponse> {
        return requestInvoker {
            api.sendHello()
        }
    }

    override suspend fun detect(link: String): Resource<DetectResponse> {
        return requestInvoker {
            api.detect(
                getAccessToken(),
                EngineDetectBody(
                    link = link,
                )
            )
        }

    }

    override suspend fun getTags(): Resource<GetTagsResponse> {
        return requestInvoker {
            api.getTags(
                getAccessToken()
            )
        }
    }

    override suspend fun getDownloads(): Resource<GetDownloadsResponse> {
        return requestInvoker {
            api.getDownloads(
                getAccessToken()
            )
        }
    }

    override suspend fun getDownloadDetail(id: String): Resource<DownloadItem> {
        TODO("not implemented")
//        return requestInvoker {
//            api.getOneDownload(
//                getAccessToken(),
//                id,
//            )
//        }
    }

    override suspend fun getCategories(): Resource<GetCategoriesResponse> {
        return requestInvoker {
            api.getCategories(
                getAccessToken()
            )
        }
    }

}