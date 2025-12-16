package top.writerpass.micromessage.utils

import java.net.InetAddress
import java.util.UUID
import kotlin.random.Random

actual fun getDeviceName(): String {
    return getHostName()
}

private fun getHostName(): String {
    return try {
        InetAddress.getLocalHost().hostName
    } catch (e: Exception) {
        System.getenv("HOSTNAME")
            ?: System.getenv("COMPUTERNAME")
            ?: "unknown-host-${Random.nextInt()}"
    }
}

actual fun generateDeviceSerial(): String {
//    return UUID.randomUUID().toString()
    return "00-00-00-00-00-00-00-00"
}