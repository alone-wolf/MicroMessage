package top.writerpass.micromessage.client

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import top.writerpass.kmplibrary.utils.println
import top.writerpass.micromessage.client.navigation.pages.Pages
import top.writerpass.micromessage.client.navigation.pages.base.IPage
import top.writerpass.micromessage.client.navigation.pages.global.LoginPage
import top.writerpass.micromessage.client.navigation.pages.main.Chat
import top.writerpass.micromessage.client.navigation.pages.main.ChatListPage
import top.writerpass.micromessage.client.viewmodels.ChatListViewModel
import top.writerpass.micromessage.client.viewmodels.ApplicationViewModel
import top.writerpass.micromessage.client.viewmodels.MicroMessageSdkViewModel

val LocalNavController = staticCompositionLocalOf<NavControllerWrapper> {
    error("No NavHostController Provided")
}

val LocalCurrentPage = staticCompositionLocalOf<Pair<IPage, Map<String, Any?>>> {
    error("No Page Provided")
}

val LocalMicroMessageSdkViewModel = staticCompositionLocalOf<MicroMessageSdkViewModel> {
    error("No MicroMessageSdkViewModel Provided")
}

val LocalApplicationViewModel = staticCompositionLocalOf<ApplicationViewModel> {
    error("No ApplicationViewModel Provided")
}

val LocalChatListViewModel = staticCompositionLocalOf<ChatListViewModel> {
    error("No ChatListViewModel provided")
}

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    return remember {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = ViewModelStore()
        }
    }
}

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState Provided")
}


class NavControllerWrapper(
    private val navHostController: NavHostController
) {
    val c = navHostController
    fun open(page: IPage, vararg args: Any) = page.open(
        navController = navHostController,
        args = *args
    )

    fun login() = open(ChatListPage)

    fun logout() = open(LoginPage)

    @Composable
    fun currentPageAsState(): State<Pair<IPage, Map<String, Any?>>> {
        val navBackStackEntry by c.currentBackStackEntryFlow.collectAsState(null)
        return remember {
            derivedStateOf {
                val arguments: Map<String, Any?> = navBackStackEntry?.arguments?.read {
                    toMap()
                } ?: emptyMap()
                val r = navBackStackEntry?.destination?.route ?: LoginPage.routeBase
                val segments = r.split("/")
                val currentRouteBase = segments.first()
                Pages.pageRouteMap[currentRouteBase]!! to arguments
            }
        }
    }

    fun popBackStack() = c.popBackStack()
}

@Composable
fun rememberNavControllerWrapper(): NavControllerWrapper {
    val c = rememberNavController()
    val d = remember {
        NavControllerWrapper(c)
    }
    return d
}