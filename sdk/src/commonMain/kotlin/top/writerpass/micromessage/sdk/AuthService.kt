@file:OptIn(ExperimentalAtomicApi::class)

package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.cancel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.idParam
import org.koin.core.component.getScopeId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.AuthStore
import top.writerpass.micromessage.ReturnBody
import top.writerpass.micromessage.ServerRoutes
import top.writerpass.micromessage.request.RegisterRequest
import top.writerpass.micromessage.response.LoginResponse
import top.writerpass.micromessage.response.SessionsResponse
import top.writerpass.micromessage.utils.WithLogger
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class AuthService(private val client: HttpClient) : WithLogger {
    suspend fun register(username: String, password: String): Boolean {
        val passwordHash0 = password
        val reqBody = RegisterRequest(username, passwordHash0)
        val r = client.post(ServerRoutes.Api.V1.Auth.Register.path) {
            contentType(ContentType.Application.Json)
            setBody(reqBody)
        }
        return r.status == HttpStatusCode.OK
    }


    @Serializable
    data class LoginRequest(
        val deviceName: String = "WPT14A",
        val deviceId: String = "00-00-00-00"
    )

    suspend fun login(username: String, password: String): Boolean {
        val r = client.post(ServerRoutes.Api.V1.Auth.Login.path) {
            basicAuth(username, password)
            setBody(LoginRequest())
        }

        val rr = r.body<ReturnBody<LoginResponse>>()
        rr.data?.let { session ->
            AuthStore.updateToken(session)
        }

        return r.status == HttpStatusCode.OK && rr.data?.token != null
    }

    suspend fun logout(): Boolean {
        val r = client.delete(ServerRoutes.Api.V1.Auth.Logout.path)
        r.bodyAsChannel().cancel()
        return r.status == HttpStatusCode.OK || r.status == HttpStatusCode.Unauthorized
    }

    suspend fun sessions(): List<SessionsResponse> {
        val r = client.get(ServerRoutes.Api.V1.Auth.Sessions.path)
        return r.body<ReturnBody<List<SessionsResponse>>>().data ?: emptyList()
    }

    suspend fun logoutSession(sessionId: Long): Boolean {
        val r = client.delete(
            ServerRoutes.Api.V1.Auth.Sessions.ById(sessionId.toString())
        )
        return r.status == HttpStatusCode.OK
    }

    override val logger: Logger = LoggerFactory.getLogger(this::class.java)
}