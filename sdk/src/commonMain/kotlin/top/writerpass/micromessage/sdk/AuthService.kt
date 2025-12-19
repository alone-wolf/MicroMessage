@file:OptIn(ExperimentalAtomicApi::class)

package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.cancel
import org.slf4j.Logger
import top.writerpass.kmplibrary.utils.println
import top.writerpass.micromessage.AuthStore
import top.writerpass.micromessage.ReturnBody
import top.writerpass.micromessage.ServerRoutes
import top.writerpass.micromessage.auth.request.DeviceHardware
import top.writerpass.micromessage.auth.request.DeviceNetwork
import top.writerpass.micromessage.auth.request.DeviceOS
import top.writerpass.micromessage.auth.request.DevicePlatform
import top.writerpass.micromessage.auth.request.DeviceRuntime
import top.writerpass.micromessage.auth.request.DeviceSecurity
import top.writerpass.micromessage.auth.request.DeviceType
import top.writerpass.micromessage.auth.request.IpVersion
import top.writerpass.micromessage.auth.request.LoginDevice
import top.writerpass.micromessage.auth.request.LoginRequest
import top.writerpass.micromessage.auth.request.RegisterRequest
import top.writerpass.micromessage.auth.response.LoginResponse
import top.writerpass.micromessage.auth.response.SessionsResponse
import top.writerpass.micromessage.utils.DeviceInfoCollector
import top.writerpass.micromessage.utils.logger
import top.writerpass.micromessage.utils.WithLogger
import top.writerpass.micromessage.utils.dump
import top.writerpass.micromessage.utils.generateDeviceSerial
import top.writerpass.micromessage.utils.getDeviceName
import top.writerpass.micromessage.utils.print
import top.writerpass.micromessage.utils.responseOk
import top.writerpass.micromessage.utils.responseUnauthorized
import top.writerpass.micromessage.utils.setJsonBody
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class AuthService(private val client: HttpClient) : WithLogger {
    override val logger: Logger = logger("AuthService")
    suspend fun register(username: String, password: String): Boolean {
        val passwordHash0 = password
        val r = client.post(ServerRoutes.Api.V1.Auth.Register.path) {
            RegisterRequest(username, passwordHash0).let(::setJsonBody)
        }
        return r.responseOk()
    }

    suspend fun login(username: String, password: String): Boolean {
        val r = client.post(ServerRoutes.Api.V1.Auth.Login.path) {
            basicAuth(username, password)

            val deviceInfo = DeviceInfoCollector.collect()
            deviceInfo.toString().println()
            LoginRequest(
                device = deviceInfo
//                device = LoginDevice(
//                    hostname = getDeviceName(),
//                    serial = generateDeviceSerial(),
//                    type = DeviceType.Desktop,
//                    os = DeviceOS(
//                        platform = DevicePlatform.Windows,
//                        version = "11 25H2",
//                        kernel = "NT10.0",
//                        arch = "X86_64"
//                    ),
//                    hardware = DeviceHardware(
//                        cpu = "M4",
//                        cores = "15",
//                        memMB = "111111",
//                        gpus = listOf("asdasdasd")
//                    ),
//                    network = listOf(
//                        DeviceNetwork(
//                            "127.0.0.1",
//                            mask = "255.255.255.0",
//                            gateway = "",
//                            version = IpVersion.IPv4,
//                        )
//                    ),
//                    security = DeviceSecurity(
//                        fingerprint = "aaaa",
//                        publicKey = "bbbbb",
//                        trusted = true
//                    ),
//                    runtime = DeviceRuntime(
//                        version = "0.0.1"
//                    )
//                )
            ).let(::setJsonBody)
        }
        val rr = r.body<ReturnBody<LoginResponse>>()
        rr.data?.let(AuthStore::updateToken)
        return r.responseOk() && rr.data?.token != null
    }

    suspend fun logout(): Boolean {
        val r = client.delete(ServerRoutes.Api.V1.Auth.Logout.path)
        r.bodyAsChannel().cancel()
        return r.responseOk() || r.responseUnauthorized()
    }

    suspend fun sessions(): List<SessionsResponse> {
        val r = client.get(ServerRoutes.Api.V1.Auth.Sessions.path)
        return r.body<ReturnBody<List<SessionsResponse>>>().data ?: emptyList()
    }

    suspend fun logoutSession(sessionId: Long): Boolean {
        val r = client.delete(
            "${ServerRoutes.Api.V1.Auth.Sessions.path}/${sessionId}"
        )
        r.dump(true).print()
        return r.responseOk()
    }
}

