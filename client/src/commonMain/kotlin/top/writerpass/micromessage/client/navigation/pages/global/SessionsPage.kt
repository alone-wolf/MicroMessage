@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.micromessage.AuthStore
import top.writerpass.micromessage.client.LocalMicroMessageSdkViewModel
import top.writerpass.micromessage.client.navigation.pages.base.IPage
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object SessionsPage : IPage {
    override val routeBase: String
        get() = "sessions"
    override val label: String
        get() = "Sessions"
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val microMessageSdkViewModel = LocalMicroMessageSdkViewModel.current

            LaunchedEffectOdd {
                microMessageSdkViewModel.sessions()
            }

            val thisSessionId = remember {
                AuthStore.getToken()?.sessionId ?: -1
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = microMessageSdkViewModel.sessionList,
                    key = { it.id } // 若没有可去掉
                ) { session ->
                    SessionItem(
                        isThisDevice = thisSessionId == session.id,
                        deviceName = session.device.name,
                        loginType = "${session.identifierType.name}&${session.credentialType.name}",
                        expiresAt = Instant.fromEpochMilliseconds(session.expiresAt),
                        onLogout = {
                            microMessageSdkViewModel.logoutSession(session.id)
                        }
                    )
                }
            }

        }

}

@OptIn(ExperimentalTime::class)
@Composable
private fun SessionItem(
    isThisDevice: Boolean,
    deviceName: String,
    loginType: String,
    expiresAt: Instant,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = deviceName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "登录方式：$loginType",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "过期时间：${expiresAt.toString()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isThisDevice.not()) {
                TextButton(
                    onClick = onLogout,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("退出")
                }
            }
        }
    }
}

