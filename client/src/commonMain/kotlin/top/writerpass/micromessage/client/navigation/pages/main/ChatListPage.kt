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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import top.writerpass.micromessage.client.LocalChatListViewModel
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
    val id: Long,
    val name: String,
    val read: Boolean = false,
    val pin: Boolean = false,
    val mute: Boolean = false,
    val hide: Boolean = false,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val chatType: ChatType = ChatType.Private
)


object ChatListPage : IMainPage {
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.Message
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.Message
    override val routeBase: String
        get() = "chat-list"
    override val label: String
        get() = "Chats"
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
            val chatListViewModel = LocalChatListViewModel.current

            val chats by chatListViewModel.chatListFlow.collectAsState()

            FullSizeBox {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    items(
                        items = chats,
                        key = { it.hashCode() },
                        itemContent = { chat ->
                            FullWidthRow(
                                modifier = Modifier
                                    .clickable {
                                        when (chat.chatType) {
                                            ChatType.Private -> {
                                                navController.open(
                                                    PrivateChatPage,
                                                    chat.id,
                                                    chat.name
                                                )
                                            }

                                            ChatType.Temp -> {

                                            }

                                            ChatType.Group -> {

                                            }

                                            ChatType.Global -> {

                                            }
                                        }

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
                            chatListViewModel.updateChat(data.id){
                                copy(read = false)
                            }
                            contextMenuState.hide()
                        }
                    )
                    DropdownMenuItem(
                        text = { "Set Mute".CxText() },
                        onClick = {
                            contextMenuState.hide()
                            chatListViewModel.updateChat(data.id){
                                copy(mute = true)
                            }
                        }
                    )
                    if (data.hide){
                        DropdownMenuItem(
                            text = { "Show".CxText() },
                            onClick = {
                                contextMenuState.hide()
                                chatListViewModel.updateChat(data.id){
                                    copy(hide = false)
                                }
                            }
                        )
                    }else{
                        DropdownMenuItem(
                            text = { "Hide".CxText() },
                            onClick = {
                                contextMenuState.hide()
                                chatListViewModel.updateChat(data.id){
                                    copy(hide = true)
                                }
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { "Delete".CxText() },
                        onClick = {
                            contextMenuState.hide()
                            chatListViewModel.removeChat(data.id)
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