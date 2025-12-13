package top.writerpass.micromessage.common.response

import kotlinx.serialization.Serializable


@Serializable
data class SessionsResponse(
    val id: Long,
    val userId: Long,
    val sessionToken: String,
    val expiresAt: Long
)
