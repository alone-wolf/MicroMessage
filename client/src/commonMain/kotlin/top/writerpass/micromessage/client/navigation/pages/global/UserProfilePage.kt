package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.navigation.pages.base.IPage

object UserProfilePage : IPage {
    override val routeBase: String
        get() = "user-profile"
    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("userId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    override val label: String
        get() = "User Profile"

    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            var userId by remember { mutableStateOf(-1L) }
            LaunchedEffectOdd {
                it.arguments?.read {
                    userId = getLong("userId")
                }
            }
            FullSizeColumn {
                "UserId: ${userId}".CxText()
                FullWidthRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        "Remark".CxText()
                        "User's Nickname".CxText()
                        "User's Id".CxText()
                        "Location Beijing".CxText()
                    }
                }
                FullWidthRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }
            }

            fun LazyListScope.infoItem(
                label: String,
                extra: @Composable () -> Unit,
                onClick: () -> Unit = {}
            ) {
                item {
                    FullWidthRow(
                        modifier = Modifier
                            .clickable(onClick = onClick)
                            .padding(vertical = 18.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            label.CxText(fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            extra()
                            Icons.Default.ArrowRight.CxIcon()
                        }
                    )
                }
            }

        }
}