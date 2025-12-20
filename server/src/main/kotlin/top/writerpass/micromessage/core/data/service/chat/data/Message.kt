package top.writerpass.micromessage.core.data.service.chat.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.chat.enums.MessageType
import top.writerpass.micromessage.core.data.service.user.table.UserTable

@Serializable
data class Message(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val type: MessageType,
    val seq:Long,
    val createdAt:Long,
)

object MessageTable: LongIdTable() {
    val chatId = long("chat_id").references(ChatTable.id)
    val senderId = long("sender_id").references(UserTable.id)
    val type = enumeration<MessageType>("type")
    val seq = long("seq")
    val createdAt = long("created_at")
}

class MessageEntity(id: EntityID<Long>): LongEntity(id){
    companion object: LongEntityClass<MessageEntity>(MessageTable)

    var chatId by MessageTable.chatId
    var senderId by MessageTable.senderId
    var type by MessageTable.type
    var seq by MessageTable.seq
    var createdAt by MessageTable.createdAt

    fun toData(): Message {
        return Message(
            id = id.value,
            chatId = chatId,
            senderId = senderId,
            type = type,
            seq = seq,
            createdAt = createdAt
        )
    }
}