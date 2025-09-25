package com.unreel.unreel.networks.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class ErrorResponse2(
//    override val message: String,
    val error: String,
//    override val statusCode: Int,
) : BaseResponse

@Serializable
data class ErrorResponse (
    val success: Boolean?,
//    override val statusCode: Int,
//    override val message: String,
    val timestamp: String? = null,
    val path: String? = null,
    val error: String? = null,
    @Contextual val data: Any? = null
) : BaseResponse
@Serializable
data class ErrorData(
    val identifier: String?,
    val token: String?
)

@Serializable
data class ErrorResponseWithListMessages(
    val success: Boolean,
//    override val statusCode: Int,
    @SerialName("message") val messageList: List<String>,
    val timestamp: String,
    val path: String,
    val error: String,
) : BaseResponse {
//    override val message: String? get() = messageList.joinToString("\n")
    fun toErrorResponse() = ErrorResponse(
        success = success,
//        statusCode = statusCode,
//        message = messageList.reduce { acc, element -> acc + "\n" + element },
        timestamp = timestamp,
        path = path,
        error = error,
    )
}
