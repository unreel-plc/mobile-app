package com.unreel.unreel.networks.models.auth

import com.unreel.unreel.networks.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetDownloadsResponse(
    @SerialName("limit")
    val limit: Int = 0,
    @SerialName("page")
    val page: Int = 0,
    @SerialName("results")
    val results: List<DownloadItem> = emptyList(),
    @SerialName("total")
    val total: Int = 0
) : BaseResponse

@Serializable
data class DownloadItem(
    @SerialName("categories")
    val categories: List<String> = emptyList(),
    @SerialName("categorizationNotes")
    val categorizationNotes: String = "",
    @SerialName("categoryConfidence")
    val categoryConfidence: Double = 0.0,
    @SerialName("channel")
    val channel: String? = null,
    @SerialName("channelFollowerCount")
    val channelFollowerCount: Int? = null,
    @SerialName("commentCount")
    val commentCount: Int? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("duration")
    val duration: String? = null,
    @SerialName("flashcardSummary")
    val flashcardSummary: String = "",
    @SerialName("_id")
    val id: String? = null,
    @SerialName("likeCount")
    val likeCount: Int? = null,
    @SerialName("link")
    val link: String? = null,
    @SerialName("metadataPath")
    val metadataPath: String? = null,
    @SerialName("platform")
    val platform: String? = null,
    @SerialName("progressPercent")
    val progressPercent: Int? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    @SerialName("taxonomyVersion")
    val taxonomyVersion: String = "",
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("transcriptText")
    val transcriptText: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("uploadDate")
    val uploadDate: String? = null,
    @SerialName("uploaderUrl")
    val uploaderUrl: String? = null,
    @SerialName("__v")
    val v: Int? = null,
    @SerialName("videoId")
    val videoId: String? = null,
    @SerialName("viewCount")
    val viewCount: Int? = null,
    @SerialName("youtubeSubtitleSrtPath")
    val youtubeSubtitleSrtPath: String? = null,
) : BaseResponse