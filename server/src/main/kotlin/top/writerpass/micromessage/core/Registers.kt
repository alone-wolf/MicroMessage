@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.core

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.plugins.origin
import io.ktor.server.request.contentType
import io.ktor.server.request.header
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.getAllRoutes
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.toMap
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.core.data.enums.Gender
import top.writerpass.micromessage.core.data.enums.Language
import top.writerpass.micromessage.utils.WithLogger
import top.writerpass.micromessage.utils.logWrapper
//import top.writerpass.micromessage.common.utils.WithLogger
//import top.writerpass.micromessage.common.utils.logWrapper
import top.writerpass.micromessage.core.data.service.auth.AuthNodes
import top.writerpass.micromessage.core.data.service.friend.data.FriendRelation
import top.writerpass.micromessage.core.data.service.friend.data.FriendRelationEntity
import top.writerpass.micromessage.core.data.service.friend.data.FriendRelationTable
import top.writerpass.micromessage.core.data.service.user.entity.UserEntity
import top.writerpass.micromessage.core.data.service.user.entity.UserProfileEntity
import top.writerpass.micromessage.friend.enums.FriendSource
import top.writerpass.micromessage.friend.enums.FriendStatus
import top.writerpass.micromessage.returnBadRequest
import top.writerpass.micromessage.returnOk
import top.writerpass.micromessage.server.ReturnBodyPlugin
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@Serializable
data class DebugDump(
    val method: String, val uri: String, val path: String,

    // Query 是多值 Map
    val queryParameters: Map<String, List<String>>,

    // Headers 也是多值 Map
    val headers: Map<String, List<String>>,

    // Cookies map
    val cookies: Map<String, String>,

    val clientIP: String?, val scheme: String,

    // origin host info
    val host_local: String?,
    val host_server: String?,
    val port_local: Int,
    val port_server: Int,

    val protocol: String,
    val contentType: String,
    val contentLength: Long?,

    // body 一律用 String 容器
    val body: String
)


class Register : WithLogger {
    override val logger: Logger = LoggerFactory.getLogger("Register")

    fun registerTables() {
        val database: Database = Singletons.databaseContainer.database
        logWrapper("register database tables") {
            transaction(database) {
                SchemaUtils.create(
                    *Singletons.classScanner.tables
                )
            }
        }
    }

    fun registerServiceModules(application: Application) {
        application.routing {
            registerRoutes()
        }.let {
            logWrapper("list route") {
                it.getAllRoutes().forEach { route ->
                    "Route: $route".logi()
                }
            }
        }
    }

    fun Routing.registerRoutes() {
        debugDumpRoute()
        route("/api") {
            post("/prepare-data") {
                val targetUserId = 1L
                newSuspendedTransaction {
                    val targetUserProfile = UserProfileEntity.findById(targetUserId)
                        ?: return@newSuspendedTransaction call.returnBadRequest("no id=${targetUserId}")

                    repeat(100) {
                        val now = Clock.System.now().toEpochMilliseconds()
                        val newUser = UserEntity.new {
                            createdAt = now
                        }
                        val newUserProfile = UserProfileEntity.new {
                            userId = newUser.id.value
                            nickname = "User${userId}"
                            avatarUrl = ""
                            gender = Gender.entries[userId.toInt() % 3]
                            bio = "no bio"
                            regionCountry = "C"
                            regionProvince = "P"
                            regionCity = "c"
                            language = Language.ZH
                            email = "user${userId}@mm.com"
                            phone = "22222222222222222"
                        }

                        val newFriendRelation = FriendRelationEntity.new {
                            userId = newUser.id.value
                            userFriendId = targetUserId
                            status = FriendStatus.entries[userId.toInt() % 5]
                            friendSource = FriendSource.entries[userId.toInt() % 5]
                            remark = "This is God called ${targetUserProfile.nickname}"
                            tag = ""
                            createdAt = now
                            updatedAt = now
                        }

                        FriendRelationEntity.new {
                            userId = targetUserId
                            userFriendId = newUser.id.value
                            status = newFriendRelation.status
                            friendSource = newFriendRelation.friendSource
                            remark = "This is Friend ${newUserProfile.nickname}"
                            tag = newFriendRelation.tag
                            createdAt = newFriendRelation.createdAt
                            updatedAt = newFriendRelation.updatedAt
                        }
                    }
                }
                call.returnOk("Ok")
            }
            route("/v1") {
                install(ReturnBodyPlugin)
                val routingList = Singletons.classScanner.routings
                routingList.forEach { routingItem ->
                    routingItem.apiRoutes(this)
                }
//                get("/friends"){
//                    val a = newSuspendedTransaction {
//                        FriendRelationEntity.find {
//                            FriendRelationTable.userId eq 1L
//                        }.toList().map { it.toData() }
//                    }
//                    call.returnOk(a)
//                }
                AuthNodes.NormalAccess.run {
                    route("/admin/") {
                        routingList.forEach { routingItem ->
                            routingItem.adminRoutes(this)
                        }
                    }
                }
            }
        }
    }
}

fun Routing.debugDumpRoute() {
    route("/debug/dump") {
        handle {
            val call = call

            // 读取 Body（自动处理 JSON / form / raw）
            val rawBody = runCatching { call.receiveText() }.getOrDefault("")

            val isBinary = rawBody.any { it.code < 32 && it !in listOf('\n', '\r', '\t') }

            val response = DebugDump(
                method = call.request.httpMethod.value,
                uri = call.request.uri,
                path = call.request.path(),

                queryParameters = call.request.queryParameters.toMap(),
                headers = call.request.headers.entries().associate { it.key to it.value },
                cookies = call.request.cookies.rawCookies,

                clientIP = call.request.origin.remoteHost,
                scheme = call.request.origin.scheme,

                host_local = call.request.origin.localHost,
                host_server = call.request.origin.serverHost,
                port_local = call.request.origin.localPort,
                port_server = call.request.origin.serverPort,

                protocol = call.request.origin.version,
                contentType = call.request.contentType().toString(),
                contentLength = call.request.header(HttpHeaders.ContentLength)?.toLongOrNull(),

                body = if (!isBinary) rawBody else "<binary-data (${rawBody.length} bytes)>"
            )

            call.respond(response)
        }
    }
}

