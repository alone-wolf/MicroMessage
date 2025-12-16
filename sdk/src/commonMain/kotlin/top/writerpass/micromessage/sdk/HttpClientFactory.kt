package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import top.writerpass.kmplibrary.utils.println
import top.writerpass.micromessage.AuthStore
import top.writerpass.micromessage.utils.dump
import top.writerpass.micromessage.utils.print
import top.writerpass.micromessage.ktorClientEngine

object HttpClientFactory {
    fun create(
        baseUrl: String,
    ): HttpClient {
        return HttpClient(ktorClientEngine()) {
            install(ContentNegotiation) {
                json(compactJson)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 5000
            }

            install(DefaultRequest) {
                url(baseUrl)
//                header(HttpHeaders.Accept, "*/*")
                userAgent("MicroMessageSdk/0.0.1")
//                header(HttpHeaders.UserAgent, "MicroMessageSdk/0.0.1")
//                contentType(ContentType.Application.Json)

                when (url.encodedPath) {
//                    "/api/v1/auth/login" -> {
//                        AuthStore.getLoginInfo()?.let { (u, p) ->
//                            basicAuth(u, p)
//                        }
//                    }

                    "/api/v1/auth/register" -> {
                        // Pass
                    }

                    "/api/v1/auth/logout" -> {
                        AuthStore.getToken()?.token?.let { t ->
                            bearerAuth(t)
                        }
                    }

                    else -> {
                        AuthStore.getToken()?.token?.let { t ->
                            bearerAuth(t)
                        }
                    }
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.value >= 300) {
                        "in HttpResponseValidator".println()
                        response.dump(collectBody = true).print()
                    }
                }
            }
        }
    }
}