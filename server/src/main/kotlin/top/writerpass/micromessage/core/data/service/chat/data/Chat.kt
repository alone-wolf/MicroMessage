package top.writerpass.micromessage.core.data.service.chat.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.chat.enums.ChatType
import top.writerpass.micromessage.core.data.service.user.table.UserTable

@Serializable
data class Chat(
    val id: Long,
    val type: ChatType,
    val chatKey: String,
    val ownerId: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    val metaData: String
)

object ChatTable : LongIdTable() {
    val type = enumeration<ChatType>("type")
    val chatKey = varchar("key", 100)
    val ownerId = long("owner_id").references(UserTable.id)
    val createdAt = long(name = "created_at")
    val updatedAt = long(name = "updated_at")
    val metadata = varchar("meta_data", 1024)
}

class ChatEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ChatEntity>(ChatTable)

    var type by ChatTable.type
    var chatKey by ChatTable.chatKey
    var ownerId by ChatTable.ownerId
    var createdAt by ChatTable.createdAt
    var updatedAt by ChatTable.updatedAt
    var metadata by ChatTable.metadata

    fun toData(): Chat {
        return Chat(
            id = id.value,
            type = type,
            chatKey = chatKey,
            ownerId = ownerId,
            createdAt = createdAt,
            updatedAt = updatedAt,
            metaData = metadata
        )
    }
}


