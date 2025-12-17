@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.core.data.service.auth

import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.bearer
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import top.writerpass.kmplibrary.utils.println
import top.writerpass.micromessage.auth.request.DevicePlatform
import top.writerpass.micromessage.auth.request.LoginRequest
import top.writerpass.micromessage.auth.enums.CredentialType
import top.writerpass.micromessage.auth.enums.IdentifierType
import top.writerpass.micromessage.utils.PasswordUtil
import top.writerpass.micromessage.utils.SessionTokenGenerator
import top.writerpass.micromessage.core.data.service.auth.data.Credential
import top.writerpass.micromessage.core.data.service.auth.data.LoginSessionEntity
import top.writerpass.micromessage.core.data.service.auth.data.LoginSessionTable
import top.writerpass.micromessage.core.data.service.auth.principal.UserInfoPrincipal
import top.writerpass.micromessage.core.data.service.device.data.DeviceEntity
import top.writerpass.micromessage.core.data.service.device.data.DeviceTable
import top.writerpass.micromessage.core.data.service.user.entity.UserIdentifierEntity
import top.writerpass.micromessage.core.data.service.user.table.UserIdentifierTable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object AuthNodes {

    interface AuthNode {
        val name: String
        val realmInfo: String
        fun AuthenticationConfig.install()
        fun Route.routeWrapper(build: Route.() -> Unit) = authenticate(
            name,
            optional = false,
            build = build
        )
    }

    object Password : AuthNode {

        override val name: String = "login"
        override val realmInfo: String = "Access to the '/api/v1/auth/login' path"

        override fun AuthenticationConfig.install() {
            basic(name) {
                realm = realmInfo
                validate { credentials ->

                    val now = Clock.System.now().toEpochMilliseconds()

                    val loginRequest = receive<LoginRequest>()
                    val deviceSerial = loginRequest.device.serial

                    val username = credentials.name
                    val passwordHash0 = credentials.password

                    val principal = newSuspendedTransaction {

                        // 1. 查 identifier
                        val identifier = UserIdentifierEntity.find {
                            (UserIdentifierTable.type eq IdentifierType.Username) and
                                    (UserIdentifierTable.content eq username)
                        }.singleOrNull()?.toData() ?: return@newSuspendedTransaction null

                        // 2. 查 password credential
                        val cred = Credential.Entity.find {
                            (Credential.CredentialTable.userId eq identifier.userId) and
                                    (Credential.CredentialTable.identifierId eq identifier.id) and
                                    (Credential.CredentialTable.type eq CredentialType.Password)
                        }.singleOrNull()?.toData() ?: return@newSuspendedTransaction null

                        // 3. 校验密码
                        val calcHash = PasswordUtil.hash(passwordHash0, cred.salt)
                        if (calcHash != cred.passwordHash) return@newSuspendedTransaction null

                        // 4. 根据 deviceSerial 查 Device
                        val device = (DeviceEntity.find {
                            DeviceTable.serial eq deviceSerial
                        }.singleOrNull() ?: DeviceEntity.new {
                            serial = deviceSerial
                            name = loginRequest.device.hostname
                            type = loginRequest.device.type
                            platform = loginRequest.device.os?.platform ?: DevicePlatform.Unknown
                            createdAt = now
                            updatedAt = now
                        }).toData()

                        // 5. 根据 deviceSerial 查登陆 Session，找到并删除
                        LoginSessionEntity.find {
                            (LoginSessionTable.userId eq identifier.userId) and
                                    (LoginSessionTable.deviceId eq device.id)
                        }.forEach { it.delete() }

                        // 6. 创建登陆 Session
                        val loginSession = LoginSessionEntity.new {
                            userId = identifier.userId
                            deviceId = device.id
                            identifierType = IdentifierType.Username
                            credentialType = CredentialType.Password
                            sessionToken = SessionTokenGenerator.generate()
                            expiresAt = now + 3600 * 1000
                        }.toData()

                        UserInfoPrincipal(
                            identifier.userId,
                            loginSession
                        )
                    }

                    principal
                }
            }
        }
    }

    object RefreshToken : AuthNode {
        override val name: String = "refresh-token"
        override val realmInfo: String = "Access to the '/api/v1/auth/refresh' path"

        override fun AuthenticationConfig.install() {
            bearer(name) {
                realm = this@RefreshToken.realmInfo
                authenticate { tokenCredential ->
                    "Authentication@refresh-login token=${tokenCredential.token}".println()
                    if (tokenCredential.token == "asdfghjkl") {
                        UserIdPrincipal(tokenCredential.token)
                    } else {
                        null
                    }
                }
            }
        }
    }

    object NormalAccess : AuthNode {
        override val name: String = "normal-access"
        override val realmInfo: String = "Access to the normal api path"

        override fun AuthenticationConfig.install() {
            bearer(name) {
                realm = realmInfo
                authenticate { tokenCredential ->
                    val token = tokenCredential.token

                    val principal = newSuspendedTransaction {
                        val loginSession = LoginSessionEntity.find {
                            LoginSessionTable.sessionToken eq token
                        }.singleOrNull() ?: return@newSuspendedTransaction null

                        val userId = loginSession.userId

                        UserInfoPrincipal(
                            userId = userId,
                            session = loginSession.toData(),
                        )
                    }

                    principal
                }
            }
        }
    }
}