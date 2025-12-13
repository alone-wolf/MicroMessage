package top.writerpass.micromessage

import java.util.concurrent.atomic.AtomicReference

object AuthStore {
    private val tokenRef =
        AtomicReference<String?>(null)

    fun updateToken(t: String) {
        tokenRef.set(t)
    }

    fun clearToken() {
        tokenRef.set(null)
    }

    fun getToken(): String? =
        tokenRef.get()
}