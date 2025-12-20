package top.writerpass.micromessage.core.data.service.chat.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.core.data.service.user.table.UserTable

@Serializable
data class PrivateChatOption(
    val id:Long,
    val chatId: Long,
    val ownerId:Long,
    val mute: Boolean,
    val pin: Boolean,
)

object PrivateChatOptionTable: LongIdTable() {
    val chatId = long("chat_id").references(ChatTable.id)
    val ownerId = long("owner_id").references(UserTable.id)
    val mute = bool("mute")
    val pin = bool("pin")
}

class PrivateChatOptionEntity(id: EntityID<Long>): LongEntity(id){
    companion object : LongEntityClass<PrivateChatOptionEntity>(PrivateChatOptionTable)

    var chatId by PrivateChatOptionTable.chatId
    var ownerId by PrivateChatOptionTable.ownerId
    var mute by PrivateChatOptionTable.mute
    var pin by PrivateChatOptionTable.pin

    fun toData(): PrivateChatOption{
        return PrivateChatOption(
            id = id.value,
            chatId = chatId,
            ownerId = ownerId,
            mute = mute,
            pin = pin
        )
    }
}