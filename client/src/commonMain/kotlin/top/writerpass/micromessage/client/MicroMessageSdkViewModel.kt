package top.writerpass.micromessage.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import top.writerpass.kmplibrary.coroutine.withContextMain
import top.writerpass.kmplibrary.utils.println
import top.writerpass.micromessage.AuthStore
import top.writerpass.micromessage.common.response.SessionsResponse

class MicroMessageSdkViewModel : BaseViewModel() {
    fun register(username: String, password: String, onSuccess: () -> Unit) {
        runInViewModel {
            if (Singleton.apiClient.auth.register(username, password)) {
                withContextMain { onSuccess() }
            }
        }
    }

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        runInViewModel {
            if (Singleton.apiClient.auth.login(username, password)) {
                withContextMain { onSuccess() }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        runInViewModel {
            val token = AuthStore.getToken()
            token?.let { t ->
                "current token: $t".println()
                if (Singleton.apiClient.auth.logout()) {
                    withContextMain { onSuccess() }
                    AuthStore.clearToken()
                }
            }
        }
    }

    var sessionList by mutableStateOf<List<SessionsResponse>>(emptyList())
        private set

    fun sessions() {
        runInViewModel {
            Singleton.apiClient.auth.sessions().let {
                withContextMain {
                    sessionList = it
                }
            }
        }
    }
}