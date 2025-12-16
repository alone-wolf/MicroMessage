package top.writerpass.micromessage.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val userId: Long,
    val username: String,
    val createdAt: Long
)
