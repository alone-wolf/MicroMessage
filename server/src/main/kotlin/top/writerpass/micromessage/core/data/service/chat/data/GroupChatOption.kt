package top.writerpass.micromessage.core.data.service.chat.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.core.data.service.user.table.UserTable

@Serializable
data class GroupChatOption(
    val id: Long,
    val chatId: Long, // chat.id
    val ownerId: Long, // User.id
    val mute: Boolean,
    val pin: Boolean,
)

object GroupChatOptionTable : LongIdTable() {
    val chatId = long("chat_id").references(ChatTable.id)
    val ownerId = long("owner_id").references(UserTable.id)
    val mute = bool("mute")
    val pin = bool("pin")
}

class GroupChatOptionEntity(id: EntityID<Long>): LongEntity(id){
    companion object: LongEntityClass<GroupChatOptionEntity>(GroupChatOptionTable)

    var chatId by GroupChatOptionTable.chatId
    var ownerId by GroupChatOptionTable.ownerId
    var mute by GroupChatOptionTable.mute
    var pin by GroupChatOptionTable.pin
}