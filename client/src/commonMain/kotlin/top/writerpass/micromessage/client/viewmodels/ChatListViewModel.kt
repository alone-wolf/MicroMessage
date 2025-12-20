package top.writerpass.micromessage.client.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import top.writerpass.micromessage.client.navigation.pages.main.Chat

class ChatListViewModel : ViewModel() {

    /** 内存中的数据源（模拟 Repository / DB） */
    private val chatList = mutableListOf(
        Chat(
            id = 1L,
            name = "张三",
            read = false,
            pin = true,
            mute = false,
            lastMessage = "你好",
            lastMessageTime = 1710000000000,
            unreadCount = 3
        ),
        Chat(
            id = 2L,
            name = "李四",
            read = true,
            pin = false,
            mute = false,
            lastMessage = "OK",
            lastMessageTime = 1710000100000,
            unreadCount = 0
        )
    )

    fun getById(id: Long): Chat? {
        return chatList.find { it.id == id }
    }

    /** 对外只暴露 StateFlow */
    private val _chatListFlow = MutableStateFlow(chatList.toList())
    val chatListFlow: StateFlow<List<Chat>> = _chatListFlow.asStateFlow()

    /* ---------------- 基础操作 ---------------- */

    fun loadAll() {
        _chatListFlow.value = chatList.sorted()
    }

    fun addChat(chat: Chat) {
        chatList.add(chat)
        emit()
    }

    fun removeChat(chatId: Long) {
        chatList.removeAll { it.id == chatId }
        emit()
    }

    fun updateChat(chatId: Long, block: Chat.() -> Chat) {
        val index = chatList.indexOfFirst { it.id == chatId }
        if (index >= 0) {
            chatList[index] = chatList[index].block()
            emit()
        }
    }

    /* ---------------- 业务语义方法 ---------------- */

    fun onNewMessage(
        chatId: Long,
        message: String,
        time: Long = System.currentTimeMillis()
    ) {
        updateChat(chatId) {
            copy(
                lastMessage = message,
                lastMessageTime = time,
                unreadCount = unreadCount + 1,
                read = false
            )
        }
    }

    fun markRead(chatId: Long) {
        updateChat(chatId) {
            copy(read = true, unreadCount = 0)
        }
    }

    fun togglePin(chatId: Long) {
        updateChat(chatId) {
            copy(pin = !pin)
        }
    }

    fun toggleMute(chatId: Long) {
        updateChat(chatId) {
            copy(mute = !mute)
        }
    }

    /* ---------------- 内部工具 ---------------- */

    private fun emit() {
        _chatListFlow.value = chatList
            .filter { it.hide.not() }
            .sorted()
    }

    /** 置顶优先，其次按时间倒序 */
    private fun List<Chat>.sorted(): List<Chat> =
        this.sortedWith(
            compareByDescending<Chat> { it.pin }
                .thenByDescending { it.lastMessageTime }
        )
}