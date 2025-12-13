package top.writerpass.micromessage.client

import top.writerpass.kmplibrary.coroutine.withContextMain
import top.writerpass.micromessage.BearerTokenStore

class MicroMessageSdkViewModel : BaseViewModel() {
    fun register(username: String, password: String, onSuccess: () -> Unit) {
        runInViewModel {
            if (Singleton.apiClient.auth.register(username, password)) {
                withContextMain { onSuccess() }
            }
        }
    }

    fun login(onSuccess: () -> Unit) {
        runInViewModel {
            if (Singleton.apiClient.auth.login()) {
                withContextMain { onSuccess() }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        runInViewModel {
            if (Singleton.apiClient.auth.logout()) {
                withContextMain {
                    onSuccess()
                }
            }
        }
    }
}