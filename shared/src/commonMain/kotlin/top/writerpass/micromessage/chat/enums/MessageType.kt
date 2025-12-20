package top.writerpass.micromessage.chat.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    Text, Image, File, System
//    Video,
//    Voice,
//    Sticker,
//    Location,
//    Contact,
//    Custom
}