package top.writerpass.micromessage.auth.request

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val device: LoginDevice,
)

@Serializable
data class LoginDevice(
    val hostname: String,
    val serial: String,
    val type: DeviceType,
    val os: DeviceOS?,
    val hardware: DeviceHardware?,
    val network: List<DeviceNetwork>?,
    val security: DeviceSecurity?,
    val runtime: DeviceRuntime
)

@Serializable
data class DeviceOS(
    val platform: DevicePlatform,
    val version: String,
    val kernel: String,
    val arch: String
)

@Serializable
data class DeviceHardware(
    val cpu: String,
    val cores: String,
    val memMB: String,
    val gpus: List<String>
)

@Serializable
data class DeviceNetwork(
    val ip: String,
    val mask: String,
    val gateway: String,
    val version: IpVersion, // v4 or v6
)

@Serializable
data class DeviceSecurity(
    val fingerprint: String,
    val publicKey: String,
    val trusted: Boolean
)

@Serializable
data class DeviceRuntime(
    val version: String
)

@Serializable
enum class IpVersion {
    IPv4, IPv6
}

@Serializable
enum class DeviceType {
    Web, Mobile, Desktop
}

@Serializable
enum class DevicePlatform {
    Windows,
    MacOS,
    Linux,
    Unix,
    Android,
    IOS,
    Web,
    Unknown
}