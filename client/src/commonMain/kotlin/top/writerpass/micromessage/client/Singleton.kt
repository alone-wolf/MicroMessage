package top.writerpass.micromessage.client

import top.writerpass.micromessage.sdk.ApiClient

object Singleton{
    val apiClient = ApiClient(
        baseUrl = "http://127.0.0.1:8080"
    )
}