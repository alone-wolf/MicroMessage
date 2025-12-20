package top.writerpass.micromessage.client.widget

import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset

@Stable
class ContextMenuState<T> {
    var expanded by mutableStateOf(false)
    var anchorOffset by mutableStateOf(IntOffset.Zero)
    var payload by mutableStateOf<T?>(null)

    fun show(offset: Offset, payload: T) {
        this.anchorOffset = IntOffset(offset.x.toInt(), offset.y.toInt())
        this.payload = payload
        this.expanded = true
    }

    fun hide() {
        expanded = false
    }
}

fun <T> Modifier.contextMenuAnchor(
    state: ContextMenuState<T>,
    payload: T
): Modifier = composed {
    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    onGloballyPositioned { layoutCoordinates = it }
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.buttons.isSecondaryPressed) {
                        val local = event.changes.first().position
                        val window = layoutCoordinates
                            ?.localToWindow(local)
                            ?: local
                        state.show(window, payload)
                    }
                }
            }
        }
}

@Composable
fun <T> ContextMenuHost(
    state: ContextMenuState<T>,
    content: @Composable (T) -> Unit
) {
    val density = LocalDensity.current

    DropdownMenu(
        expanded = state.expanded,
        onDismissRequest = { state.hide() },
        offset = with(density) {
            DpOffset(
                state.anchorOffset.x.toDp(),
                state.anchorOffset.y.toDp()
            )
        }
    ) {
        state.payload?.let { content(it) }
    }
}


