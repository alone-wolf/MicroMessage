package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IPage


object PrivateChatDetailPage : IPage {
    override val routeBase: String
        get() = "private-chat-detail"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("chatId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )

    override val label: String
        get() = "Chat Detail Page"
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            val navController = LocalNavController.current
            Icons.Default.Person.CxIconButton {
                navController.open(UserProfilePage, -1)
            }
        }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            "Chat Detail".CxText()
        }

}
