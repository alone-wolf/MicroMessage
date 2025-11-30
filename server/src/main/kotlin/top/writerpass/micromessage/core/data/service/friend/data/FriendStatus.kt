package top.writerpass.micromessage.core.data.service.friend.data

import kotlinx.serialization.Serializable

@Serializable
enum class
FriendStatus {
    Pending, Accepted, Rejected
}