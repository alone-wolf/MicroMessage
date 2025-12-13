@file:OptIn(ExperimentalAtomicApi::class)

package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.BearerTokenStore
import top.writerpass.micromessage.LoginCredentialStore
import top.writerpass.micromessage.common.ReturnBody
import top.writerpass.micromessage.common.ServerRoutes
import top.writerpass.micromessage.common.request.RegisterRequest
import top.writerpass.micromessage.common.response.LoginResponse
import top.writerpass.micromessage.common.utils.WithLogger
import top.writerpass.micromessage.common.utils.dump
import top.writerpass.micromessage.common.utils.print
import top.writerpass.micromessage.ktorClientEngine
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object HttpClientFactory {
    fun create(
        baseUrl: String,
    ): HttpClient {
        return HttpClient(ktorClientEngine()) {
            install(Auth) {
                // register - no auth
                // login - basic
                // logout - bearer
                val basicPaths = arrayOf(
                    "/api/v1/auth/login",
                )
                basic {
                    credentials {
                        val u = LoginCredentialStore.username
                        val p = LoginCredentialStore.passwordHash0
                        if (u != null && p != null) {
                            BasicAuthCredentials(u, p)
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest { req ->
                        req.url.encodedPath in basicPaths
                    }
                }
                bearer {
                    loadTokens {
                        val t = BearerTokenStore.token
                        if (t != null) {
                            BearerTokens(
                                accessToken = t,
                                refreshToken = ""
                            )
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest { req ->
                        val p = req.url.encodedPath
                        p != "/api/v1/auth/login" &&
                                p != "/api/v1/auth/register"
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    explicitNulls = false
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 5000
            }

            defaultRequest {
                url(baseUrl)
//                header(HttpHeaders.Accept, "*/*")
                header(HttpHeaders.UserAgent, "MicroMessageSdk/0.0.1")
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.value >= 300) {
                        response.dump(collectBody = true).print()
//                        val text = response.bodyAsText()
//                        throw ApiException(response.status, text)
                    }
                }
            }
        }
    }
}

class ApiException(val status: HttpStatusCode, val text: String) : Throwable() {

}

class ApiClient(
    baseUrl: String,
) {
    private val client = HttpClientFactory.create(baseUrl = baseUrl)

    suspend fun requestDebugDump() {
        val r = client.post("/debug/dump")
        val b = r.bodyAsText()
        print(b)
    }

    val auth = AuthService(client)
//    val user = UserService(client)
//    val message = MessageService(client)
}

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

    suspend fun login(): Boolean {
        val r = client.post(ServerRoutes.Api.V1.Auth.Login.path)
//        r.dump().print()
        val rr = r.body<ReturnBody<LoginResponse>>()
        rr.data?.token?.let { token ->
            BearerTokenStore.token = token
        }
        return r.status == HttpStatusCode.OK
    }

    suspend fun logout(): Boolean {
        return if (BearerTokenStore.token != null) {
            val r = client.delete(ServerRoutes.Api.V1.Auth.Logout.path)
//            r.dump().print()
            val rr = r.status == HttpStatusCode.OK
            if (rr) BearerTokenStore.token = null
            rr
        } else {
            false
        }
    }

    override val logger: Logger = LoggerFactory.getLogger(this::class.java)
}