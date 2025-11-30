package top.writerpass.micromessage.core.data.service.user.request

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginRequest(
    val identifier: String,
    val secret: String,
)