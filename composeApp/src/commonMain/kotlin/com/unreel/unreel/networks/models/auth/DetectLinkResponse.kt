package com.unreel.unreel.networks.models.auth

import com.unreel.unreel.networks.models.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class DetectLinkResponse(
    val user: String? = null, // ObjectId reference to User (stored as string)

    val link: String,

    val platform: Platform,

    val videoId: String? = null,
    val title: String? = null,
    val thumbnail: String? = null,
    val duration: Int? = null,
    val description: String? = null,
    val filePath: String? = null,
    val metadataPath: String? = null,

    // Platform-specific metadata
    val uploaderUrl: String? = null,
    val channel: String? = null,
    val channelFollowerCount: Int? = null,
    val viewCount: Int? = null,
    val likeCount: Int? = null,
    val repostCount: Int? = null,
    val commentCount: Int? = null,
    val uploadDate: String? = null, // YYYYMMDD
    val youtubeSubtitleSrtPath: String? = null,
    val flashcardSummary: String? = null,
    val transcriptText: String? = null,

    // Categorization fields
    val categories: List<String>? = null,
    val tags: List<String>? = null,
    val categoryConfidence: Double? = null,
    val categorizationNotes: String? = null,
    val taxonomyVersion: String? = null,

    // Processing status
    val status: Status = Status.QUEUED,
    val errorMessage: String? = null,
    val progressPercent: Int = 0
): BaseResponse

@Serializable
enum class Platform {
    FACEBOOK,
    INSTAGRAM,
    YOUTUBE,
    TIKTOK,
    UNKNOWN
}

@Serializable
enum class Status {
    QUEUED,
    PROCESSING,
    COMPLETED,
    FAILED
}