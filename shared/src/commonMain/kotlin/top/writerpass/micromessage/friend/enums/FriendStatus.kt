package top.writerpass.micromessage.friend.enums

import kotlinx.serialization.Serializable

@Serializable
enum class
FriendStatus {
    Pending, // 好友请求验证中
    Accepted, // 好友请求已通过，现在是朋友了
    Rejected, // 好友请求未通过
    Blocked, // 不论是不是好友，已经屏蔽，可以手动解除屏蔽，被屏蔽的用户再发出消息或者好友请求都不再被接收（将提供屏蔽列表供用户管理）
    Deleted // 好友已删除（单向删除）
}