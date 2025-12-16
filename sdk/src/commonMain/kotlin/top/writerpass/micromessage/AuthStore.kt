package top.writerpass.micromessage

import top.writerpass.micromessage.auth.response.LoginResponse
import java.util.concurrent.atomic.AtomicReference

object AuthStore {
    private val tokenRef =
        AtomicReference<LoginResponse?>(null)

    fun updateToken(loginResponse: LoginResponse) {
        tokenRef.set(loginResponse)
    }

    fun clearToken() {
        tokenRef.set(null)
    }

    fun getToken(): LoginResponse? =
        tokenRef.get()
}