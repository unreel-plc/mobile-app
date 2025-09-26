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
): BaseResponse

@Serializable
data class DownloadItem(
    @SerialName("categories")
    val categories: List<String> = emptyList(),
    @SerialName("categorizationNotes")
    val categorizationNotes: String = "",
    @SerialName("categoryConfidence")
    val categoryConfidence: Double = 0.0,
    @SerialName("channel")
    val channel: String,
    @SerialName("channelFollowerCount")
    val channelFollowerCount: Int,
    @SerialName("commentCount")
    val commentCount: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("flashcardSummary")
    val flashcardSummary: String = "",
    @SerialName("_id")
    val id: String,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("link")
    val link: String,
    @SerialName("metadataPath")
    val metadataPath: String,
    @SerialName("platform")
    val platform: String,
    @SerialName("progressPercent")
    val progressPercent: Int,
    @SerialName("status")
    val status: String,
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    @SerialName("taxonomyVersion")
    val taxonomyVersion: String = "",
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("transcriptText")
    val transcriptText: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("uploadDate")
    val uploadDate: String,
    @SerialName("uploaderUrl")
    val uploaderUrl: String,
    @SerialName("__v")
    val v: Int,
    @SerialName("videoId")
    val videoId: String,
    @SerialName("viewCount")
    val viewCount: Int,
    @SerialName("youtubeSubtitleSrtPath")
    val youtubeSubtitleSrtPath: String
): BaseResponse