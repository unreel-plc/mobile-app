package com.unreel.unreel.networks.repository
import com.unreel.unreel.core.common.utils.Resource
import com.unreel.unreel.networks.models.auth.DetectLinkResponse
import com.unreel.unreel.networks.models.auth.DetectResponse
import com.unreel.unreel.networks.models.auth.DownloadItem
import com.unreel.unreel.networks.models.auth.GetCategoriesResponse
import com.unreel.unreel.networks.models.auth.GetDownloadsResponse
import com.unreel.unreel.networks.models.auth.GetTagsResponse
import com.unreel.unreel.networks.models.auth.LoginResponse
import com.unreel.unreel.networks.models.auth.HelloResponse


interface RemoteRepository {
    suspend fun login(
        token: String,
    ): Resource<LoginResponse>

    suspend fun sendHello(): Resource<HelloResponse>

    suspend fun detect(link: String): Resource<DetectResponse>

    suspend fun getTags(): Resource<GetTagsResponse>

    suspend fun getDownloads(): Resource<GetDownloadsResponse>

    suspend fun getCategories(): Resource<GetCategoriesResponse>

    suspend fun getDownloadDetail(id: String): Resource<DownloadItem>
}

