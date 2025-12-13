package top.writerpass.micromessage.core.data.service.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class SessionsResponse(
    val id: Long,
    val expiresAt: Long
)
