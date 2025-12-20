package top.writerpass.micromessage.friend.enums

import kotlinx.serialization.Serializable

@Serializable
enum class FriendSource {
    SearchName, SearchPhone, SearchEmail, Group, QRCode,
}