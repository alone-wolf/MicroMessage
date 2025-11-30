import kotlinx.coroutines.runBlocking
import top.writerpass.micromessage.sdk.ApiClient

/**
 * 1. 登录/注册/重置密码 vv
 * 2. 主 Tab
 *    - 消息列表 v
 *    - 联系人 v
 *    - 发现 v
 *    - 我的 v
 * 3. 聊天界面
 *    - 私聊消息 v
 *    - 群聊消息
 *    - 私聊详情
 *    - 群聊详情
 * 5. 用户详情
 * 6. 添加好友
 * 7. 搜索页（全局/聊天内）
 * 8. 多媒体查看器
 * 9. 文件管理中心
 * 10. 通话（语音/视频）
 * 11. 账户与设置页
 * 12. 设备管理
 * 13. 消息收藏/归档
 * 14. 朋友圈/动态
 * 15. 系统通知中心
 * 16. 高级管理（支付、钱包等）
 */

fun main() {
    val baseUrl = "http://127.0.0.1:8080"
    val client = ApiClient(
        baseUrl = baseUrl
    )

    runBlocking {
        val username = "writerpass"
        val password = "abc123"
//        client.requestDebugDump()
        client.auth.register(username, password)
    }
}


