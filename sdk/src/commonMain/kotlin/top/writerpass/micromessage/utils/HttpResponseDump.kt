package top.writerpass.micromessage.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

data class HttpResponseDump(
    val method: HttpMethod,
    val url: String,
    val status: HttpStatusCode,
    val contentType: ContentType?,
    val responseHeaders: Map<String, List<String>>,
    val requestHeaders: Map<String, List<String>>,
    val body: String
)

suspend inline fun HttpResponse.dump(
    maxBodyLength: Int = 4 * 1024,
    collectBody: Boolean = false,
    headerWhitelist: Set<String>? = null
): HttpResponseDump {
    val filteredRequestHeaders = request.headers.entries()
        .filter { headerWhitelist == null || it.key in headerWhitelist }
        .associate { it.key to it.value }

    val ct = contentType()

    val bodyText = if (collectBody) {
        val rawBytes = bodyAsChannel()
            .readRemaining(maxBodyLength.toLong())
            .readByteArray()

        when {
            ct == null ->
                "<no content-type>"

            ct.match(ContentType.Application.Json) || ct.match(ContentType.Text.Any) ->
                rawBytes.toString(Charsets.UTF_8)

            else ->
                "<binary body ${rawBytes.size} bytes>"
        }
    } else {
        "collectBody==false"
    }

    val filteredResponseHeaders = headers.entries()
        .filter { headerWhitelist == null || it.key in headerWhitelist }
        .associate { it.key to it.value }

    return HttpResponseDump(
        method = call.request.method,
        url = call.request.url.toString(),
        status = status,
        contentType = ct,
        responseHeaders = filteredResponseHeaders,
        requestHeaders = filteredRequestHeaders,
        body = bodyText
    )
}

fun HttpResponseDump.print() {
    println("===== HTTP Response =====")
    println("Request : $method $url")
    println("Status  : $status")
    println("Type    : $contentType")

    println("Request Headers:")
    requestHeaders.forEach { (k, v) ->
        println("  $k: ${v.joinToString()}")
    }

    println("Response Headers:")
    responseHeaders.forEach { (k, v) ->
        println("  $k: ${v.joinToString()}")
    }

    println("Body:")
    println(body)
    println("=========================")
}

