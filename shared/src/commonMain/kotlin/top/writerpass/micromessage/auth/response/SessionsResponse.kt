package top.writerpass.micromessage.auth.response

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.auth.enums.LoginType
import top.writerpass.micromessage.auth.request.DevicePlatform
import top.writerpass.micromessage.auth.request.DeviceType


@Serializable
data class SessionsResponse(
    val id: Long,
    val device: SessionDeviceInfo,
    val loginType: LoginType,
    // TODO: loginAt:Long
    val expiresAt: Long
)

@Serializable
data class SessionDeviceInfo(
    val id: Long,
    val name:String,
    val serial:String,
    val type: DeviceType,
    val platform: DevicePlatform
)
