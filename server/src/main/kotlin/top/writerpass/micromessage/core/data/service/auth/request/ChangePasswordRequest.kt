package top.writerpass.micromessage.core.data.service.auth.request

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)