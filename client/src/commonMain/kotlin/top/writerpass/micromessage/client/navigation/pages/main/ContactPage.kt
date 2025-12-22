package top.writerpass.micromessage.client.navigation.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Percent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalMicroMessageSdkViewModel
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IMainPage
import top.writerpass.micromessage.client.navigation.pages.global.UserProfilePage


object ContactPage : IMainPage {
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.List
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.List
    override val routeBase: String
        get() = "contact"
    override val label: String
        get() = "Contact"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = { Icons.Default.AddCircle.CxIconButton { } }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val microMessageSdkViewModel = LocalMicroMessageSdkViewModel.current
            val navControllerWrapper = LocalNavController.current
            LaunchedEffectOdd {
                microMessageSdkViewModel.getFriends()
            }

            FullSizeBox {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = microMessageSdkViewModel.friendList,
                        key = { it.id },
                        itemContent = {
                            FullWidthRow(
                                modifier = Modifier
                                    .clickable {
                                        navControllerWrapper.open(
                                            UserProfilePage,
                                            it.userFriendId
                                        )
                                    }
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icons.Default.Percent.CxIcon()
                                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                                it.remark.CxText()
                            }
                        }
                    )
                }
            }
        }
}