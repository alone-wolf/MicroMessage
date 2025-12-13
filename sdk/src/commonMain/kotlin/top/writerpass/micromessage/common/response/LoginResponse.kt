package top.writerpass.micromessage.common.response

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    val token: String
)
