package top.writerpass.micromessage.core.data.service.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val userId: Long,
    val username: String,
    val createdAt: Long
)
