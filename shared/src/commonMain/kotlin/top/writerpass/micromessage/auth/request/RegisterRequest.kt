package top.writerpass.micromessage.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val passwordHash0: String,
)