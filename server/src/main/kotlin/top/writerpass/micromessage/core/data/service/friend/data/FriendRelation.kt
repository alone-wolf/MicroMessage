package top.writerpass.micromessage.core.data.service.friend.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.core.data.service.user.table.UserTable
import top.writerpass.micromessage.friend.enums.FriendSource
import top.writerpass.micromessage.friend.enums.FriendStatus

@Serializable
data class FriendRelation(
    val id: Long,
    val userId: Long,
    val userFriendId: Long,
    val status: FriendStatus,
    val source: FriendSource,
    val remark: String,
    val tag: List<String>,
    val createdAt: Long,
    val updatedAt: Long
)

object FriendRelationTable : LongIdTable() {
    val userId = long("user_id").references(UserTable.id)
    val userFriendId = long("user1_id").references(UserTable.id)
    val status = enumeration<FriendStatus>("friend_status")
    val friendSource = enumeration<FriendSource>("friend_source")
    val remark = varchar("remark", 255)
    val tag = text("tag")
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}

class FriendRelationEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FriendRelationEntity>(FriendRelationTable)

    var userId by FriendRelationTable.userId
    var userFriendId by FriendRelationTable.userFriendId
    var status by FriendRelationTable.status
    var friendSource by FriendRelationTable.friendSource
    var remark by FriendRelationTable.remark
    var tag by FriendRelationTable.tag
    var createdAt by FriendRelationTable.createdAt
    var updatedAt by FriendRelationTable.updatedAt

    fun toData(): FriendRelation {
        return FriendRelation(
            id = id.value,
            userId = userId,
            userFriendId = userFriendId,
            status = status,
            source = friendSource,
            remark = remark,
            tag = tag.split("|"),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

