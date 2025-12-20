package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import org.slf4j.Logger
import top.writerpass.micromessage.utils.WithLogger
import top.writerpass.micromessage.utils.logger

class ApiClient(baseUrl: String) {
    private val client = HttpClientFactory.create(baseUrl = baseUrl)

//    suspend fun requestDebugDump() {
//        val r = client.post("/debug/dump")
//        val b = r.bodyAsText()
//        print(b)
//    }

    val auth = AuthService(client)
    val user = UserService(client)
//    val message = MessageService(client)
}

// 🧱 基础层（非业务）
// AuthService
// DeviceService
// ConnectionService   // WebSocket / Realtime
// SyncService

// 👤 社交关系层
// UserService
// FriendService
// GroupService

// 💬 会话与消息层（核心）
// ChatService        // 会话
// MessageService     // 消息

// 📞 实时音视频
// CallService        // audio / video / signaling

// 📦 媒体与文件
// MediaService       // file / image / voice / video

// 🔔 系统与横切能力
// NotificationService
// Metrics / Logger（非业务 Service）

class UserService(private val client: HttpClient) : WithLogger {
    override val logger: Logger = logger("UserService")

    suspend fun getUserBaseInfoWithId(userId: Long) {

    }
}

class FriendService(private val client: HttpClient) : WithLogger {
    override val logger: Logger = logger("FriendService")
    suspend fun getFriendsWithUserId(userId: Long) {
    }

    suspend fun getFriendWithId(friendId: Long) {

    }

    suspend fun requestFriend() {

    }

}

class ChatService(private val client: HttpClient) : WithLogger {
    override val logger: Logger = logger("ChatService")

    suspend fun getUserBaseInfoWithId(userId: Long) {

    }
}
