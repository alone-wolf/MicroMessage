package top.writerpass.micromessage.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.cancel
import org.slf4j.Logger
import top.writerpass.micromessage.ReturnBody
import top.writerpass.micromessage.ServerRoutes
import top.writerpass.micromessage.friend.response.FriendListResponse
import top.writerpass.micromessage.utils.WithLogger
import top.writerpass.micromessage.utils.logger

class ApiClient(baseUrl: String) {
    private val client = HttpClientFactory.create(baseUrl = baseUrl)

//    suspend fun requestDebugDump() {
//        val r = client.post("/debug/dump")
//        val b = r.bodyAsText()
//        print(b)
//    }

    suspend fun prepareData(){
        val r = client.post("/api/prepare-data")
        r.bodyAsChannel().cancel()
    }

    val auth = AuthService(client)
    val user = UserService(client)
    val friend = FriendService(client)
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
    suspend fun getUserFriends(): ReturnBody<List<FriendListResponse>> {
        val r = client.get(ServerRoutes.Api.V1.Friend.path)
        return r.body<ReturnBody<List<FriendListResponse>>>()
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
