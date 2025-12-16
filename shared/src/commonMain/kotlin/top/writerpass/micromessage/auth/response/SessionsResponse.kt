package top.writerpass.micromessage.auth.response

import kotlinx.serialization.Serializable


@Serializable
data class SessionsResponse(
    val id: Long,
    // TODO: deviceId
    // TODO: loginAt:Long
    val expiresAt: Long
)
