package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.micromessage.client.LocalMicroMessageSdkViewModel
import top.writerpass.micromessage.client.navigation.pages.base.IPage

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

            FullSizeColumn {
                microMessageSdkViewModel.sessionList.forEach { items ->
                    Text(items.expiresAt.toString())
                }
            }
        }

}
