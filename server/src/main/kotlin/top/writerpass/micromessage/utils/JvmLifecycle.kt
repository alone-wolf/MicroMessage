package top.writerpass.micromessage.utils

interface JvmLifecycle {
    suspend fun start()
    suspend fun stop()
}