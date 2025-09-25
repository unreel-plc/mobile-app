package com.unreel.unreel.networks.models.auth

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
@Serializable
data class ErrorResponse2(
    val message: String,
    val error: String,
    val statusCode: Int,
)

@Serializable
data class ErrorResponse (
    val success: Boolean?,
    val statusCode: Int,
    val message: String,
    val timestamp: String? = null,
    val path: String? = null,
    val error: String? = null,
    val data : ErrorData? = null
)
@Serializable
data class ErrorData(
    val identifier: String?
)

@Serializable
data class ErrorResponseWithListMessages(
    val success: Boolean,
    val statusCode: Int,
    val message: List<String>,
    val timestamp: String,
    val path: String,
    val error: String,
) {
    fun toErrorResponse() = ErrorResponse(
        success = success,
        statusCode = statusCode,
        message = message.reduce { acc, element -> acc + "\n" + element },
        timestamp = timestamp,
        path = path,
        error = error,
    )
}
