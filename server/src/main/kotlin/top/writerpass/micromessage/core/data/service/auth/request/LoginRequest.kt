package top.writerpass.micromessage.core.data.service.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val deviceName: String,
    val deviceId: String
)