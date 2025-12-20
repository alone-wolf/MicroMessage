package top.writerpass.micromessage.chat.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ChatType {
    Private, Temp, Group, Global
}
