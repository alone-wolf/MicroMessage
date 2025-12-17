package top.writerpass.micromessage.core.data.service.auth.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.auth.enums.LoginType
import top.writerpass.micromessage.core.data.base.BaseDataClass
import top.writerpass.micromessage.core.data.service.device.data.DeviceTable
import top.writerpass.micromessage.core.data.service.user.table.UserTable

@Serializable
data class LoginSession(
    override val id: Long,
    val userId: Long,
    val deviceId: Long,
    val loginType: LoginType,
    val sessionToken: String,
    val expiresAt: Long
) : BaseDataClass

object LoginSessionTable : LongIdTable() {
    val userId = long("user_id").references(ref = UserTable.id)

    val deviceId = long("device_id").references(ref = DeviceTable.id)

    val loginType = enumeration<LoginType>("login_type")
    val sessionToken = varchar(
        name = "session_token",
        length = 100
    ).uniqueIndex()
    val expiresAt = long("expires_at")
}

class LoginSessionEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<LoginSessionEntity>(LoginSessionTable)

    var userId by LoginSessionTable.userId
    var deviceId by LoginSessionTable.deviceId

    var loginType by LoginSessionTable.loginType
    var sessionToken by LoginSessionTable.sessionToken
    var expiresAt by LoginSessionTable.expiresAt

    fun toData(): LoginSession {
        return LoginSession(
            id = id.value,
            userId = userId,
            deviceId = deviceId,
            loginType = loginType,
            sessionToken = sessionToken,
            expiresAt = expiresAt
        )
    }
}