package top.writerpass.micromessage.core.data.service.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val passwordHash0: String,
)