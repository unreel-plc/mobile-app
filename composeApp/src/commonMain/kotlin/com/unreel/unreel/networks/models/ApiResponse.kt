package com.unreel.unreel.networks.models

import com.unreel.unreel.networks.models.auth.DownloadItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.Json

val customJson = Json {
    isLenient = true
    ignoreUnknownKeys = true
}

@Serializable
data class ApiResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: JsonElement? = null,
    @SerialName("meta")
    val meta: JsonElement? = null,
    @SerialName("links")
    val links: JsonElement? = null,
    @SerialName("error")
    val error: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("path")
    val path: String? = null,
) : BaseResponse {

    inline fun <reified R> getDataAs(json: Json = customJson): R? {
        return try {
            when {
                data == null -> null
                data is JsonObject && data.isEmpty() -> null // Handle empty object {}
                else -> json.decodeFromJsonElement<R>(data)
            }
        } catch (e: Exception) {
            println("Failed to decode data as ${R::class.simpleName}: ${e.message}")
            null
        }
    }

    inline fun <reified M> getMetaAs(json: Json = Json.Default): M? {
        return try {
            meta?.let { json.decodeFromJsonElement<M>(it) }
        } catch (e: Exception) {
            println("Failed to decode meta: ${e.message}")
            null
        }
    }

    inline fun <reified L> getLinksAs(json: Json = Json.Default): L? {
        return try {
            links?.let { json.decodeFromJsonElement<L>(it) }
        } catch (e: Exception) {
            println("Failed to decode links: ${e.message}")
            null
        }
    }
}

typealias ListApiResponse<T> = ApiResponse<List<T>>

typealias ObjectApiResponse<T> = ApiResponse<T>