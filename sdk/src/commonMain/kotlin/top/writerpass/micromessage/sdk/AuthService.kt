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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.cancel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.kmplibrary.utils.println
import top.writerpass.micromessage.AuthStore
import top.writerpass.micromessage.common.ReturnBody
import top.writerpass.micromessage.common.ServerRoutes
import top.writerpass.micromessage.common.request.RegisterRequest
import top.writerpass.micromessage.common.response.LoginResponse
import top.writerpass.micromessage.common.response.SessionsResponse
import top.writerpass.micromessage.common.utils.WithLogger
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

    suspend fun login(username: String, password: String): Boolean {
        val r = client.post(ServerRoutes.Api.V1.Auth.Login.path) {
            basicAuth(username, password)
        }

        val rr = r.body<ReturnBody<LoginResponse>>()
        rr.data?.token?.let { token -> AuthStore.updateToken(token) }

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

    override val logger: Logger = LoggerFactory.getLogger(this::class.java)
}