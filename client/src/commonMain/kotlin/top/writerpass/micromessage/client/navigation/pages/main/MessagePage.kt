package top.writerpass.micromessage.client.navigation.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.chat.enums.ChatType
import top.writerpass.micromessage.client.LocalApplicationViewModel
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IMainPage
import top.writerpass.micromessage.client.navigation.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.navigation.pages.global.SearchPage
import top.writerpass.micromessage.client.widget.ContextMenuHost
import top.writerpass.micromessage.client.widget.ContextMenuState
import top.writerpass.micromessage.client.widget.PlatformScrollBar
import top.writerpass.micromessage.client.widget.contextMenuAnchor

@Serializable
data class Chat(
    val name:String,
    val read: Boolean,
    val pin: Boolean,
    val mute: Boolean,
    val lastMessage:String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val chatType: ChatType = ChatType.Private
)

private val chatList = listOf(
    Chat(
        name = "张三",
        read = false,
        pin = true,
        mute = true,
        lastMessage = "Icons.AutoMirrored.Outlined.Message",
        lastMessageTime = 1111111111111,
        unreadCount = 10
    ),
    Chat(
        name = "李四",
        read = true,
        pin = true,
        mute = true,
        lastMessage = "object MessagePage : IMainPage",
        lastMessageTime = 2221111111111,
        unreadCount = 0
    ),
    Chat(
        name = "王五",
        read = false,
        pin = false,
        mute = false,
        lastMessage = "今晚一起吃饭？",
        lastMessageTime = 2222222222222,
        unreadCount = 3
    ),
    Chat(
        name = "赵六",
        read = true,
        pin = false,
        mute = false,
        lastMessage = "收到，我这边已处理",
        lastMessageTime = 2223333333333,
        unreadCount = 0
    ),
    Chat(
        name = "钱七",
        read = false,
        pin = true,
        mute = false,
        lastMessage = "PR 已经提了，帮忙看下",
        lastMessageTime = 2224444444444,
        unreadCount = 2
    ),
    Chat(
        name = "孙八",
        read = true,
        pin = false,
        mute = true,
        lastMessage = "😂😂😂",
        lastMessageTime = 2225555555555,
        unreadCount = 0
    ),
    Chat(
        name = "周九",
        read = false,
        pin = false,
        mute = false,
        lastMessage = "文档我已经更新到 Confluence",
        lastMessageTime = 2226666666666,
        unreadCount = 5
    ),
    Chat(
        name = "吴十",
        read = true,
        pin = false,
        mute = false,
        lastMessage = "OK，没问题",
        lastMessageTime = 2227777777777,
        unreadCount = 0
    ),
    Chat(
        name = "郑十一",
        read = false,
        pin = false,
        mute = true,
        lastMessage = "明天几点开会？",
        lastMessageTime = 2228888888888,
        unreadCount = 1
    ),
    Chat(
        name = "王十二",
        read = true,
        pin = true,
        mute = false,
        lastMessage = "Release 已发布到生产",
        lastMessageTime = 2229999999999,
        unreadCount = 0
    ),
    Chat(
        name = "冯十三",
        read = false,
        pin = false,
        mute = false,
        lastMessage = "这个问题我复现了一下",
        lastMessageTime = 2231111111111,
        unreadCount = 4
    ),
    Chat(
        name = "陈十四",
        read = true,
        pin = false,
        mute = false,
        lastMessage = "下周我请假两天",
        lastMessageTime = 2232222222222,
        unreadCount = 0
    )
)


object MessagePage : IMainPage {
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.Message
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.Message
    override val routeBase: String
        get() = "message"
    override val label: String
        get() = "Message"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            val applicationViewModel = LocalApplicationViewModel.current
            if (applicationViewModel.pinMainWindowOnTop) {
                Icons.Filled.PushPin.CxIconButton {
                    applicationViewModel.pinMainWindowOnTop = false
                }
            } else {
                Icons.Outlined.PushPin.CxIconButton {
                    applicationViewModel.pinMainWindowOnTop = true
                }
            }
            Icons.Default.AddCircleOutline.CxIconButton {}
        }
    override val fab: @Composable (() -> Unit)
        get() = {
            val navController = LocalNavController.current
            FloatingActionButton(
                onClick = {
                    navController.open(SearchPage)
                },
                content = {
                    Icons.Default.Search.CxIcon()
                }
            )
        }
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val navController = LocalNavController.current
            val contextMenuState = remember { ContextMenuState<Chat>() }
            val lazyListState = rememberLazyListState()

            FullSizeBox {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    items(
                        items = chatList,
                        key = { it.hashCode() },
                        itemContent = { chat->
                            FullWidthRow(
                                modifier = Modifier
                                    .clickable {
                                        navController.open(PrivateChatPage, 100)
                                    }
                                    .contextMenuAnchor(
                                        state = contextMenuState,
                                        payload = chat
                                    )
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icons.Default.Person2.CxIcon(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    chat.name.CxText(
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    chat.lastMessage.CxText(
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    )
//                    items(100) { index ->
//                        FullWidthRow(
//                            modifier = Modifier
//                                .clickable {
//                                    navController.open(PrivateChatPage, index)
//                                }
//                                .contextMenuAnchor(
//                                    state = contextMenuState,
//                                    payload = index
//                                )
//                                .padding(
//                                    horizontal = 12.dp,
//                                    vertical = 8.dp
//                                ),
//                            verticalAlignment = Alignment.CenterVertically,
//                        ) {
//                            Icons.Default.Person2.CxIcon(
//                                modifier = Modifier
//                                    .size(48.dp)
//                                    .clip(RoundedCornerShape(6.dp))
//                            )
//                            Column(modifier = Modifier.weight(1f)) {
//                                "用户${index}用户${index}用户${index}用户${index}".CxText(
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontSize = 16.sp,
//                                    fontWeight = FontWeight.Normal
//                                )
//                                "[18] 这是一条测试消息".CxText(
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.Light
//                                )
//                            }
//                        }
//                    }
                }
                PlatformScrollBar(
                    lazyListState = lazyListState
                )

                ContextMenuHost(
                    state = contextMenuState
                ) { data ->
                    DropdownMenuItem(
                        text = { "Pin".CxText() },
                        onClick = {
                            contextMenuState.hide()
                        }
                    )
                    DropdownMenuItem(
                        text = { "Set Unread".CxText() },
                        onClick = {
                            contextMenuState.hide()
                        }
                    )
                    DropdownMenuItem(
                        text = { "Set Mute".CxText() },
                        onClick = {
                            contextMenuState.hide()
                        }
                    )
                    DropdownMenuItem(
                        text = { "Hide".CxText() },
                        onClick = {
                            contextMenuState.hide()
                        }
                    )
                    DropdownMenuItem(
                        text = { "Delete".CxText() },
                        onClick = {
                            contextMenuState.hide()
                        }
                    )
                }
            }
        }

    override fun open(navController: NavController, vararg args: Any) {
        navController.navigate(routeBase) {
            navController.popBackStack()
        }
    }
}