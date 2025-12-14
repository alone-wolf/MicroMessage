package top.writerpass.micromessage.core.data.service.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val sessionId: Long, // sessionId
)