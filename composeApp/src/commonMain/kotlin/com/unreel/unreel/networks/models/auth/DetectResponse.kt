package com.unreel.unreel.networks.models.auth

import com.unreel.unreel.networks.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DetectResponse(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("_id")
    val id: String,
    @SerialName("link")
    val link: String,
    @SerialName("platform")
    val platform: String,
    @SerialName("progressPercent")
    val progressPercent: Int,
    @SerialName("status")
    val status: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("__v")
    val v: Int
): BaseResponse