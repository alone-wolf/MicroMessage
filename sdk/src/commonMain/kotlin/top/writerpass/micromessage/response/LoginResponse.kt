package top.writerpass.micromessage.response

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val sessionId: Long
)
