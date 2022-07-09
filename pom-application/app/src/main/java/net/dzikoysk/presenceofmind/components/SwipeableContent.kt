package net.dzikoysk.presenceofmind.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class SwipeState {
    LEFT,
    CONTENT,
    RIGHT
}

typealias SnapTo = (SwipeState) -> Unit
typealias SwipeableContent = (SwipeState, SnapTo) -> Unit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCard(
    menuSize: Dp,
    menuBackgroundColor: Color,
    leftContent: @Composable SwipeableContent?,
    content: @Composable SwipeableContent,
    rightEntry: @Composable SwipeableContent?
) {
    val swipeAbleState = rememberSwipeableState(initialValue = SwipeState.CONTENT)
    val contentSize = remember { mutableStateOf(IntSize(0, 0)) }
    val sizePx = with(LocalDensity.current) { menuSize.toPx() }

    val scope = rememberCoroutineScope()
    val swipeTo: (SwipeState) -> Unit = {
        scope.launch {
            swipeAbleState.snapTo(it)
        }
    }

    val anchors = mutableMapOf<Float, SwipeState>()
    rightEntry?.run { anchors.put(-sizePx, SwipeState.RIGHT) }
    anchors.put(0f, SwipeState.CONTENT)
    leftContent?.run { anchors.put(sizePx, SwipeState.LEFT) }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeAbleState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(fraction = menuSize.value) },
                orientation = Orientation.Horizontal
            )
            .background(menuBackgroundColor, shape = RoundedCornerShape(CornerSize(9.dp))),
    ) {
        leftContent?.also {
            Column(
                modifier = Modifier
                    .width(menuSize)
                    .height(LocalDensity.current.run { contentSize.value.height.toDp() }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = { it(swipeAbleState.currentValue, swipeTo) }
            )
        }
        Column(
            modifier = Modifier
                .offset { // NOTE: offset has to be the first value in modifier to properly notify background
                    IntOffset(
                        x = swipeAbleState.offset.value.roundToInt(),
                        y = 0
                    )
                }
                .onGloballyPositioned { contentSize.value = it.size }
                .background(MaterialTheme.colors.surface),
            content = { content(swipeAbleState.currentValue, swipeTo) }
        )
        rightEntry?.also {
            Column(
                modifier = Modifier
                    .width(menuSize)
                    .height(LocalDensity.current.run { contentSize.value.height.toDp() })
                    .offset {
                        IntOffset(
                            x = swipeAbleState.offset.value.roundToInt() + contentSize.value.width,
                            y = 0
                        )
                    }
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = { it(swipeAbleState.currentValue, swipeTo) }
            )
        }
    }
}

fun Modifier.drawTaskColorAccent(color: Color): Modifier =
    drawWithContent {
        drawContent()
        clipRect {
            drawLine(
                brush = SolidColor(color),
                strokeWidth = 45f,
                cap = StrokeCap.Square,
                start = Offset.Zero,
                end = Offset.Zero.copy(y = size.height)
            )
        }
    }