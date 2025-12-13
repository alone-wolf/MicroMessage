package top.writerpass.micromessage.common

import kotlinx.serialization.Serializable

@Serializable
class ReturnBody<T>(
    val code: Int,
    val message: String,
    val data: T?,
    val error: String?
)