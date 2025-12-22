@file:OptIn(ExperimentalComposeUiApi::class)

package top.writerpass.cmplibrary.modifier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned

fun Modifier.onPointerRightClick(block:(Offset)-> Unit): Modifier = composed {
    onPointerEvent(PointerEventType.Press) { e ->
        if (e.buttons.isSecondaryPressed) {
            val pos = e.changes.first().position
            block(pos)
        }
    }
}

fun Modifier.onPointerRightClickInWindow(
    onClick: (Offset) -> Unit
): Modifier = composed {
    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    this
        .onGloballyPositioned { coords ->
            layoutCoordinates = coords
        }
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.buttons.isSecondaryPressed) {
                        val localPos = event.changes.first().position
                        val windowPos = layoutCoordinates
                            ?.localToWindow(localPos)
                            ?: localPos
                        onClick(windowPos)
                    }
                }
            }
        }
}
