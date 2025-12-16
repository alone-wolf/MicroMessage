package top.writerpass.micromessage.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

fun HttpResponse.responseOk(): Boolean {
    return status == HttpStatusCode.OK
}

fun HttpResponse.responseUnauthorized(): Boolean {
    return status == HttpStatusCode.Unauthorized
}