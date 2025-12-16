package top.writerpass.micromessage.utils

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

inline fun <reified T> HttpRequestBuilder.setJsonBody(data: T) {
    setBody(data)
    contentType(ContentType.Application.Json)
}