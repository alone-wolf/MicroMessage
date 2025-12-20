package top.writerpass.micromessage.core.data.service.chat.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.chat.enums.ChatMemberRole
import top.writerpass.micromessage.core.data.service.user.table.UserTable

@Serializable
data class ChatMember(
    val id: Long,
    val chatId: Long,
    val userId: Long,
    val role: ChatMemberRole,
    val jointAt: Long,
    val leftAt: Long?,
)

object ChatMemberTable: LongIdTable() {
    val chatId = long("chat_id").references(ChatTable.id)
    val userId = long("user_id").references(UserTable.id)
    val role = enumeration<ChatMemberRole>("role")
    val jointAt = long("joint_at")
    val leftAt = long("left_at").nullable()
}

class ChatMemberEntity(id: EntityID<Long>): LongEntity(id){
    companion object: LongEntityClass<ChatMemberEntity>(ChatMemberTable)

    var chatId by ChatMemberTable.chatId
    var userId by ChatMemberTable.userId
    var role by ChatMemberTable.role
    var jointAt by ChatMemberTable.jointAt
    var leftAt by ChatMemberTable.leftAt
}