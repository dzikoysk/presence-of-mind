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

data class SwipeContext(
    val currentState: () -> SwipeState,
    val snapTo: SnapTo,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCard(
    swipeThreshold: Float,
    menuBackgroundColor: Color,
    onStateChange: @Composable ((SwipeContext) -> Unit) = {},
    leftContent: @Composable SwipeableContent? = null,
    content: @Composable SwipeableContent,
    rightContent: @Composable SwipeableContent? = null
) {
    val scope = rememberCoroutineScope()

    val contentSize = remember { mutableStateOf(IntSize(1, 1)) }
    val swipeThresholdInDp = with(LocalDensity.current) { contentSize.value.width.toDp() }
    val contentWidthInDp = with(LocalDensity.current) { contentSize.value.height.toDp() }

    val anchors = mutableMapOf<Float, SwipeState>()
    rightContent?.run { anchors.put(-contentSize.value.width * swipeThreshold, SwipeState.RIGHT) }
    anchors[0f] = SwipeState.CONTENT
    leftContent?.run { anchors.put(contentSize.value.width * swipeThreshold, SwipeState.LEFT) }

    val previousSwipeState = remember { mutableStateOf(SwipeState.CONTENT) }
    val swipeableState = rememberSwipeableState(initialValue = SwipeState.CONTENT)
    val swipeContext = remember {
        SwipeContext(
            currentState = { swipeableState.currentValue },
            snapTo = {
                scope.launch {
                    swipeableState.snapTo(it)
                }
            }
        )
    }

    if (swipeableState.currentValue != previousSwipeState.value) {
        onStateChange(swipeContext)
        previousSwipeState.value = swipeableState.currentValue
    }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(fraction = 0.9f) },
                orientation = Orientation.Horizontal,
                velocityThreshold = swipeThresholdInDp
            )
            .background(menuBackgroundColor, shape = RoundedCornerShape(CornerSize(9.dp))),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(swipeThresholdInDp)
        ) {
            Column(
                modifier = Modifier
                    .width(56.dp)
                    .height(contentWidthInDp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    leftContent?.also {
                        it(swipeableState.currentValue, swipeContext.snapTo)
                    }
                }
            )
            Column(
                modifier = Modifier
                    .width(56.dp)
                    .height(contentWidthInDp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    rightContent?.also {
                        it(swipeableState.currentValue, swipeContext.snapTo)
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .offset { // NOTE: offset has to be the first value in modifier to properly notify background
                    IntOffset(
                        x = swipeableState.offset.value.roundToInt(),
                        y = 0
                    )
                }
                .onGloballyPositioned { contentSize.value = it.size }
                .background(MaterialTheme.colors.surface),
            content = { content(swipeableState.currentValue, swipeContext.snapTo) }
        )
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