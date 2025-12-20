package top.writerpass.micromessage.friend.response

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.friend.enums.FriendSource
import top.writerpass.micromessage.friend.enums.FriendStatus


@Serializable
data class FriendListResponse(
    val id:Long,
    val userFriendId:Long,
    val status: FriendStatus,
    val source: FriendSource,
    val remark:String,
    val tag:List<String>,
    val createdAt: Long,
    val updatedAt: Long
)
