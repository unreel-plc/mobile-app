package com.unreel.unreel.networks.models.auth

import com.unreel.unreel.networks.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String? = null,
    @SerialName("user")
    val user: UserModel? = null
) : BaseResponse

@Serializable
data class UserModel(
    @SerialName("_id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("username")
    val username: String,
    @SerialName("role")
    val role: String,
    @SerialName("isVerified")
    val isVerified: Boolean,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("tier")
    val tier: String,
    @SerialName("karma_points")
    val karmaPoints: Int,
    @SerialName("picture")
    val picture: String,
    @SerialName("given_name")
    val givenName: String,
    @SerialName("family_name")
    val familyName: String,
    @SerialName("sub")
    val sub: String,
    @SerialName("iss")
    val iss: String,
    @SerialName("azp")
    val azp: String,
    @SerialName("aud")
    val aud: String,
    @SerialName("email_verified")
    val emailVerified: Boolean,
    @SerialName("iat")
    val iat: Long,
    @SerialName("exp")
    val exp: Long
)

@Serializable
data class HelloResponse(
    @SerialName("message")
    val message: String,
) : BaseResponse

@Serializable
data class LoginData(
    @SerialName("identifier")
    val identifier: String,
)


@Serializable
data class LoginBody(
    @SerialName("token")
    val identifier: String
)

@Serializable
data class EngineDetectBody(
    @SerialName("link")
    val link: String,
)

