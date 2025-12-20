package top.writerpass.micromessage.chat.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ChatMemberRole {
    Owner, Admin, Member
}