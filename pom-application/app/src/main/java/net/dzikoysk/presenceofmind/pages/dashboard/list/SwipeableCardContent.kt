package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.task.MarkedAs
import net.dzikoysk.presenceofmind.task.isDone
import kotlin.math.roundToInt

enum class SWIPE_STATE {
    FINISH,
    CONTENT,
    EDIT
}

val SWIPE_SQUARE_SIZE = 55.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCardContent(
    taskColor: Color,
    context: TaskCardContext
) {
    val scope = rememberCoroutineScope()
    val swipeAbleState = rememberSwipeableState(initialValue = SWIPE_STATE.CONTENT)
    val contentSize = remember { mutableStateOf(IntSize(0, 0)) }
    val sizePx = with(LocalDensity.current) { SWIPE_SQUARE_SIZE.toPx() }
    val anchors = mapOf(
        0f to SWIPE_STATE.CONTENT,
        sizePx to SWIPE_STATE.FINISH,
        -sizePx to SWIPE_STATE.EDIT
    )

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeAbleState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(fraction = SWIPE_SQUARE_SIZE.value) },
                orientation = Orientation.Horizontal
            )
            .background(taskColor, shape = RoundedCornerShape(CornerSize(9.dp))),
    ) {
        // Finish menu
        Column(
            modifier = Modifier
                .width(SWIPE_SQUARE_SIZE)
                .height(LocalDensity.current.run { contentSize.value.height.toDp() }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TaskItemSwipeMenu(
                modifier = Modifier.padding(horizontal = 16.dp),
                isVisible = swipeAbleState.currentValue == SWIPE_STATE.FINISH,
                isDone = context.task.isDone(),
                onDone = {
                    val markedAsDone = if (context.task.isDone()) MarkedAs.UNFINISHED else MarkedAs.DONE

                    context.task
                        .copy(
                            doneDate = if (context.task.isDone()) null else System.currentTimeMillis(),
                            doneCount = context.task.doneCount + markedAsDone.ordinal
                        )
                        //.let { taskItemCard.onDone(it, markedAsDone) } TODO: notify task supervisor
                        .apply { context.updateTask(this) }

                    scope.launch {
                        swipeAbleState.snapTo(SWIPE_STATE.CONTENT)
                    }
                }
            )
        }
        // Content
        Column(
            modifier = Modifier
                .offset { // NOTE: offset has to be the first value in modifier to properly notify background
                    IntOffset(
                        x = swipeAbleState.offset.value.roundToInt(),
                        y = 0
                    )
                }
                .onGloballyPositioned { contentSize.value = it.size }
                .background(MaterialTheme.colors.surface)
        ) {
            TaskCardContent(
                task = context.task,
                updateTask = context.updateTask,
                deleteTask = context.deleteTask,
                // content = { taskItemCard.content() }, TODO: specific task impl
            )
        }
        // Edit menu
        Column(
            modifier = Modifier
                .width(SWIPE_SQUARE_SIZE)
                .height(LocalDensity.current.run { contentSize.value.height.toDp() })
                .offset {
                    IntOffset(
                        x = swipeAbleState.offset.value.roundToInt() + contentSize.value.width,
                        y = 0
                    )
                }
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(Modifier.padding(horizontal = 18.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                    contentDescription = "Done",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .fillMaxSize()
                        .clickable {
                            if (swipeAbleState.currentValue == SWIPE_STATE.EDIT) {
                                // open menu

                                scope.launch {
                                    swipeAbleState.snapTo(SWIPE_STATE.CONTENT)
                                }
                            }
                        },
                )
            }
        }
    }
}