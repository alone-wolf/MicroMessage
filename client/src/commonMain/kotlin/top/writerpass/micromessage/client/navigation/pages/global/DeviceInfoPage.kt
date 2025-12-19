package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.ktor.util.Platform
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.auth.request.DeviceHardware
import top.writerpass.micromessage.auth.request.DeviceNetwork
import top.writerpass.micromessage.auth.request.DeviceOS
import top.writerpass.micromessage.auth.request.DevicePlatform
import top.writerpass.micromessage.auth.request.DeviceRuntime
import top.writerpass.micromessage.auth.request.DeviceSecurity
import top.writerpass.micromessage.auth.request.DeviceType
import top.writerpass.micromessage.auth.request.IpVersion
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IPage
import top.writerpass.micromessage.utils.DeviceInfoCollector
import top.writerpass.micromessage.utils.generateDeviceSerial
import top.writerpass.micromessage.utils.getDeviceName

object DeviceInfoPage : IPage {
    override val routeBase: String
        get() = "device-info"
    override val label: String
        get() = "Device Information"

    override val showTopAppBar: Boolean
        get() = true

    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val deviceInfoViewModel = viewModel { DeviceInfoViewModel() }

            FullSizeColumn {
                "HostName: ${deviceInfoViewModel.hostname}".CxText()
                "Serial: ${deviceInfoViewModel.serial}".CxText()
                "Type: ${deviceInfoViewModel.deviceType.name}".CxText()
                deviceInfoViewModel.os?.let {os->
                    "OS Platform: ${os.platform.name}".CxText()
                    "OS Version: ${os.version}".CxText()
                    "OS Kernel: ${os.kernel}".CxText()
                    "OS Arch: ${os.arch}".CxText()
                }
                deviceInfoViewModel.hardware?.let { hardware ->
                    "Hardware CPU: ${hardware.cpu}".CxText()
                    "Hardware CPU Cores: ${hardware.cores}".CxText()
                    "Hardware Memory: ${hardware.memMB}".CxText()
                    "Hardware GPU: ${hardware.gpus}".CxText()
                }
                deviceInfoViewModel.networks.forEach { network ->
                    "Network IP: ${network.ip}".CxText()
                }
                deviceInfoViewModel.security?.let { security ->
                    "Security Fingerprint: ${security.fingerprint}".CxText()
                }
                "App Version: ${deviceInfoViewModel.runtime.version}".CxText()
            }
        }
}

class DeviceInfoViewModel : ViewModel() {
    var hostname = getDeviceName()
    var serial = generateDeviceSerial()
    var deviceType: DeviceType = DeviceType.Desktop
    var os: DeviceOS? = DeviceOS(
        platform = DevicePlatform.Windows,
        version = "11 25H2",
        kernel = "NT10.0",
        arch = "X86_64"
    )
    var hardware: DeviceHardware? = DeviceHardware(
        cpu = "M4",
        cores = "15",
        memMB = "111111",
        gpus = listOf("asdasdasd")
    )

    var networks = listOf(
        DeviceNetwork(
            "127.0.0.1",
            mask = "255.255.255.0",
            gateway = "",
            version = IpVersion.IPv4,
        )
    )

    var security: DeviceSecurity? = DeviceSecurity(
        fingerprint = "aaaa",
        publicKey = "bbbbb",
        trusted = true
    )

    var runtime = DeviceRuntime(
        version = "0.0.1"
    )

    init {
        val deviceInfo = DeviceInfoCollector.collect()
        hostname = deviceInfo.hostname
        serial = deviceInfo.serial
        deviceType = deviceInfo.type
        os = deviceInfo.os
        hardware = deviceInfo.hardware
        networks = deviceInfo.network ?: emptyList()
        security = deviceInfo.security
        runtime = deviceInfo.runtime
    }
}