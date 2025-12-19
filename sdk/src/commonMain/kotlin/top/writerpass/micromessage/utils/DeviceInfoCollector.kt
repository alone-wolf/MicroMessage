package top.writerpass.micromessage.utils

import top.writerpass.micromessage.auth.request.DeviceHardware
import top.writerpass.micromessage.auth.request.DeviceNetwork
import top.writerpass.micromessage.auth.request.DeviceOS
import top.writerpass.micromessage.auth.request.DevicePlatform
import top.writerpass.micromessage.auth.request.DeviceRuntime
import top.writerpass.micromessage.auth.request.DeviceSecurity
import top.writerpass.micromessage.auth.request.DeviceType
import top.writerpass.micromessage.auth.request.IpVersion
import top.writerpass.micromessage.auth.request.LoginDevice
import java.net.Inet4Address
import java.net.NetworkInterface
import java.security.MessageDigest

object DeviceInfoCollector {
    fun collect(): LoginDevice {
        return LoginDevice(
            hostname = getDeviceName(),
            serial = generateDeviceSerial(),
            type = DeviceType.Desktop,
            os = getOS(),
            hardware = getHardware(),
            network = NetworkUtils.getNetworks(),
            security = DeviceSecurity(
                fingerprint = DeviceFingerprint.generate(),
                publicKey = "",
                trusted = true
            ),
            runtime = DeviceRuntime("0.0.1")
        )
    }


    fun getOS(): DeviceOS {
        val name = System.getProperty("os.name").lowercase()
        val platform = when {
            name.contains("win") -> DevicePlatform.Windows
            name.contains("mac") -> DevicePlatform.MacOS
            name.contains("linux") -> DevicePlatform.Linux
            else -> DevicePlatform.Unknown
        }

        return DeviceOS(
            platform = platform,
            version = System.getProperty("os.version"),
            kernel = name,
            arch = System.getProperty("os.arch")
        )
    }

    fun getHardware(): DeviceHardware {
        val runtime = Runtime.getRuntime()
        val memMB = runtime.maxMemory() / 1024 / 1024

        return DeviceHardware(
            cpu = System.getenv("PROCESSOR_IDENTIFIER")
                ?: System.getProperty("os.arch"),
            cores = runtime.availableProcessors().toString(),
            memMB = memMB.toString(),
            gpus = GpuUtils.detectGpus()
        )
    }
}

object GpuUtils {

    fun detectGpus(): List<String> {
        return when {
            isWindows() -> exec("wmic path win32_VideoController get name")
            isLinux() -> exec("lspci | grep -i vga")
            isMac() -> exec("system_profiler SPDisplaysDataType")
            else -> emptyList()
        }
    }

    private fun exec(cmd: String): List<String> =
        try {
            ProcessBuilder(*cmd.split(" ").toTypedArray())
                .start()
                .inputStream
                .bufferedReader()
                .readLines()
                .filter { it.isNotBlank() }
        } catch (e: Exception) {
            emptyList()
        }

    private fun isWindows() = System.getProperty("os.name").contains("Windows")
    private fun isLinux() = System.getProperty("os.name").contains("Linux")
    private fun isMac() = System.getProperty("os.name").contains("Mac")
}

object NetworkUtils {

    fun getNetworks(): List<DeviceNetwork> {
        return NetworkInterface.getNetworkInterfaces()
            .toList()
            .flatMap { ni ->
                ni.interfaceAddresses.mapNotNull { addr ->
                    val inet = addr.address ?: return@mapNotNull null
                    if (inet.isLoopbackAddress) return@mapNotNull null

                    DeviceNetwork(
                        ip = inet.hostAddress,
                        mask = prefixToMask(addr.networkPrefixLength),
                        gateway = null,
                        version = if (inet is Inet4Address)
                            IpVersion.IPv4 else IpVersion.IPv6
                    )
                }
            }
    }

    private fun prefixToMask(prefix: Short): String? {
        if (prefix <= 0) return null
        val mask = (-0x1 shl (32 - prefix.toInt()))
        return listOf(
            mask shr 24 and 0xff,
            mask shr 16 and 0xff,
            mask shr 8 and 0xff,
            mask and 0xff
        ).joinToString(".")
    }
}

object DeviceFingerprint {

    fun generate(): String {
        val raw = listOf(
            getDeviceName(),
            generateDeviceSerial(),
            System.getProperty("os.name"),
            System.getProperty("os.arch")
        ).joinToString("|")

        return MessageDigest
            .getInstance("SHA-256")
            .digest(raw.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}



