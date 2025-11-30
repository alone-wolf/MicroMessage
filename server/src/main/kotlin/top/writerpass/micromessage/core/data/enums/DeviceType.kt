package top.writerpass.micromessage.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class DeviceType {
    Android,
    IOS,
    Windows,
    MacOS,
    Linux,
    Web,
    Unknown
}