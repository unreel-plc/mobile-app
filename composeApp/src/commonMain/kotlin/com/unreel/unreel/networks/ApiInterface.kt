package com.unreel.unreel.networks

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
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import kotlinx.serialization.Serializable


interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET("/")
    suspend fun sendHello(): HelloResponse

    @Headers("Content-Type: application/json")
    @POST("app/auth/refresh")
    suspend fun refresh(@Body body: RefreshBody): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("auth/google-token")
    suspend fun login(
        @Body body: LoginBody
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("engine/detect")
    suspend fun detect(
        @Header("Authorization") token: String,
        @Body body: EngineDetectBody
    ): DetectResponse

    @Headers("Content-Type: application/json")
    @GET("engine/tags")
    suspend fun getTags(
        @Header("Authorization") token: String,
    ): GetTagsResponse

    @Headers("Content-Type: application/json")
    @GET("engine/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String,
    ): GetCategoriesResponse

    @Headers("Content-Type: application/json")
    @GET("engine/downloads")
    suspend fun getDownloads(
        @Header("Authorization") token: String,
    ): GetDownloadsResponse

    @Headers("Content-Type: application/json")
    @GET("engine/{id}")
    suspend fun getOneDownload(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): DownloadItem
}

@Serializable
data class RefreshBody(
    val refreshToken: String
)