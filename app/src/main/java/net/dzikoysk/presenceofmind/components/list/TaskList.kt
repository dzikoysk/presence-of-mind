package net.dzikoysk.presenceofmind.components.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.NamedDivider
import net.dzikoysk.presenceofmind.task.*
import org.burnoutcrew.reorderable.*
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@ExperimentalMaterialApi
@ExperimentalTime
@ExperimentalAnimationApi
@Composable
@Preview(showBackground = true)
fun TaskList(
    taskService: TaskService = TaskService().also { it.createDefaultTasks() },
) {
    val tasks = taskService.getObservableListOfAllTasks()
    val todoTasks = tasks.filter { it.done == null }
    val doneTasks = tasks.filter { it.done != null }

    TaskOrderedListColumn(
        taskService = taskService,
        indexOfTask = { tasks.indexOfFirst { task -> task.id == it } },
        tasks = todoTasks
    )
    NamedDivider(
        name = "~ Done ~",
        modifier = Modifier.padding(horizontal = 30.dp)
    )
    TaskOrderedListColumn(
        taskService = taskService,
        indexOfTask = { tasks.indexOfFirst { task -> task.id == it } },
        tasks = doneTasks
    )
}

@ExperimentalMaterialApi
@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun TaskOrderedListColumn(
    taskService: TaskService,
    indexOfTask: (UUID) -> Int,
    tasks: List<Task>
) {
    val listOrderState = rememberReorderState()

    LazyColumn(
        state = listOrderState.listState,
        modifier = Modifier
            .reorderable(
                state = listOrderState,
                onMove = { from, to ->
                    taskService.moveTasks(
                        from = indexOfTask(from.key as UUID),
                        to = indexOfTask(to.key as UUID)
                    )
                },
                onDragEnd = { _, _ -> taskService.forceTasksSave() }
            )
            .fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 22.dp,
            vertical = 16.dp
        )
    ) {
        items(
            items = tasks,
            key = { it.id },
            itemContent = { task ->
                TaskListItem(
                    state = listOrderState,
                    context = TaskItemContext(
                        task = task,
                        updateTask = { taskService.updateTask(it) },
                        deleteTask = { taskService.deleteTask(task.id) }
                    )
                )
            }
        )
    }
}

data class TaskItemContext(
    val task: Task,
    val updateTask: (Task) -> Unit = {},
    val deleteTask: () -> Unit = {}
)

@ExperimentalMaterialApi
@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun TaskListItem(
    previewMode: Boolean = false,
    state: ReorderableState = rememberReorderState(),
    context: TaskItemContext
) {
    val taskColor =
        context.task.metadata.policy.color
            .takeIf { context.task.done == null }
            ?: Color.Gray

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .draggedItem(state.offsetByKey(context.task.id))
            .detectReorder(state),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(7.dp)),
        backgroundColor = taskColor
    ) {
        Box(
            modifier = Modifier
                .drawWithContent {
                    drawContent()
                    clipRect {
                        drawLine(
                            brush = SolidColor(taskColor),
                            strokeWidth = 45f,
                            cap = StrokeCap.Square,
                            start = Offset.Zero,
                            end = Offset.Zero.copy(y = size.height)
                        )
                    }
                },
            content = {
                TaskItemSwipeableCard(
                    previewMode = previewMode,
                    context = context
                )
            }
        )
    }
}

private val SWIPE_SQUARE_SIZE = 55.dp

@ExperimentalAnimationApi
@ExperimentalTime
@ExperimentalMaterialApi
@Composable
fun TaskItemSwipeableCard(
    previewMode: Boolean = false,
    context: TaskItemContext
) {
    val scope = rememberCoroutineScope()
    val swipeAbleState = rememberSwipeableState(initialValue = 0)
    val cardHeight = remember { mutableStateOf(0) }
    val sizePx = with(LocalDensity.current) { SWIPE_SQUARE_SIZE.toPx() }
    val anchors = mapOf(
        0f to 0,
        sizePx to 1
    )

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeAbleState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(fraction = 0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .width(SWIPE_SQUARE_SIZE - (16.dp * 2))
                .height(LocalDensity.current.run { cardHeight.value.toDp() }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                TaskItemSwipeMenu(
                    context = context,
                    isVisible = swipeAbleState.currentValue == 1,
                    close = {
                        scope.launch {
                            swipeAbleState.snapTo(0)
                        }
                    }
                )
            }
        )
        Column(
            modifier = Modifier
                .offset { // NOTE: offset has to be the first value in modifier to properly notify background
                    IntOffset(
                        x = swipeAbleState.offset.value.roundToInt(),
                        y = 0
                    )
                }
                .onGloballyPositioned { cardHeight.value = it.size.height }
                .background(Color.White),
            content = {
                TaskItemCardContent(
                    previewMode = previewMode,
                    context = context
                )
            }
        )
    }
}

@Composable
fun TaskItemSwipeMenu(
    context: TaskItemContext,
    isVisible: Boolean,
    close: () -> Unit,
) {
    val isDone = context.task.done != null

    val iconId =
        if (isDone)
            R.drawable.ic_baseline_replay_24
        else
            R.drawable.ic_baseline_check_24

    Icon(
        painter = painterResource(id = iconId),
        contentDescription = "Done",
        tint = Color.White,
        modifier = Modifier
            .size(40.dp)
            .fillMaxSize()
            .clickable {
                if (isVisible) {
                    context.updateTask(
                        context.task.copy(
                            done = if (isDone) null else System.currentTimeMillis()
                        )
                    )
                    close()
                }
            },
    )
}

@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun TaskItemCardContent(previewMode: Boolean, context: TaskItemContext) {
    TaskHeader(
        deleteTask = context.deleteTask,
        content = {
            when (val metadata = context.task.metadata) {
                is LongTermMetadata -> LongTermTaskItem(context.task, metadata)
                is OneTimeMetadata -> OneTimeTaskItem(context.task, metadata)
                is RepetitiveMetadata -> RepetitiveTaskItem(
                    previewMode = previewMode,
                    task = context.task,
                    metadata = metadata
                )
            }
        },
    )
    SubTaskList(
        task = context.task
    )
}