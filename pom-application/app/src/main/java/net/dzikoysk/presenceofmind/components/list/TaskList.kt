package net.dzikoysk.presenceofmind.components.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.NamedDivider
import net.dzikoysk.presenceofmind.components.editor.SubtaskManagerDialog
import net.dzikoysk.presenceofmind.components.editor.TaskEditorDialog
import net.dzikoysk.presenceofmind.shared.mirror.LinearProgressIndicator
import net.dzikoysk.presenceofmind.task.*
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.IntervalAttribute
import org.burnoutcrew.reorderable.*
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.minutes

enum class MarkedAs {
    UNFINISHED,
    DONE
}

@Composable
@Preview(showBackground = true)
fun TaskList(
    taskService: TaskService = TaskService().also { it.createDefaultTasks() },
    displayMode: MarkedAs = MarkedAs.UNFINISHED

) {
    val tasks = taskService.getObservableListOfAllTasks()
    val doneTasks = tasks.filter { it.isDone() }

    val matchedTasks = when (displayMode) {
        MarkedAs.UNFINISHED -> tasks.filterNot { it.isDone() }
        MarkedAs.DONE -> doneTasks
    }

    val percent = runCatching { doneTasks.size / tasks.size.toFloat() }
        .getOrDefault(1f)

    LaunchedEffect(Unit) {
        while(true) {
            delay(1.minutes)
            taskService.refreshTasksState()
        }
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(top = 18.dp)
            .fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = percent,
            color = Color(0xFFADA6F8),
            modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
        )
    }
    TaskOrderedListColumn(
        taskService = taskService,
        indexOfTask = { tasks.indexOfFirst { task -> task.id == it } },
        tasks = matchedTasks
    )

    if (matchedTasks.isEmpty()) {
        NamedDivider(
            name = "No matched tasks in this group",
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp)
        )
    }
}

@Composable
fun TaskOrderedListColumn(
    taskService: TaskService,
    indexOfTask: (UUID) -> Int,
    tasks: List<Task>
) {
    val listOrderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            taskService.moveTasks(
                from = indexOfTask(from.key as UUID),
                to = indexOfTask(to.key as UUID)
            )
        },
        onDragEnd = { _, _ -> taskService.forceTasksSave() }
    )

    val height =
        if (tasks.isEmpty())
            Modifier.height(0.dp)
        else
            Modifier.fillMaxHeight()

    LazyColumn(
        state = listOrderState.listState,
        modifier = Modifier
            .reorderable(listOrderState)
            .fillMaxWidth()
            .then(height),
        contentPadding = PaddingValues(
            horizontal = 22.dp,
            vertical = 16.dp
        )
    ) {
        items(
            items = tasks,
            key = { it.id },
            itemContent = { task ->
                ReorderableItem(
                    reorderableState = listOrderState,
                    key = task.id
                ) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)

                    TaskListItem(
                        state = listOrderState,
                        context = TaskItemContext(
                            task = task,
                            updateTask = { taskService.saveTask(it) },
                            deleteTask = { taskService.deleteTask(task.id) }
                        ),
                        modifier = Modifier
                            .shadow(elevation.value)
                            .background(Color.Transparent)
                    )
                }
            }
        )
    }
}

data class TaskItemContext(
    val task: Task,
    val updateTask: (Task) -> Unit = {},
    val deleteTask: () -> Unit = {},
    val onDone: () -> Unit = {}
)

@Composable
fun TaskListItem(
    modifier: Modifier = Modifier,
    state: ReorderableLazyListState = rememberReorderableLazyListState(onMove = { _, _ -> }),
    context: TaskItemContext,
) {
    val taskColor =
        context.task.metadata.type.color
            .takeUnless { context.task.isDone() }
            ?: Color.Gray

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .detectReorder(state)
            .then(modifier),
        elevation = 0.dp,
        shape = RoundedCornerShape(CornerSize(8.dp)),
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
                    taskColor = taskColor,
                    context = context
                )
            }
        )
    }
}

data class TaskItemCard(
    val content: @Composable (() -> Unit),
    val onDone: (Task, MarkedAs) -> Task = { task, _ -> task }
)

private val SWIPE_SQUARE_SIZE = 55.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItemSwipeableCard(
    taskColor: Color,
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

    @Suppress("UNCHECKED_CAST")
    val taskItemCard = when (val metadata = context.task.metadata) {
        is LongTermMetadata ->
            createLongTermTaskItem(
                task = context.task
            )
        is EventAttribute ->
            createOneTimeTaskItem(
                task = context.task,
                metadata = metadata
            )
        is IntervalAttribute ->
            createRepetitiveTaskItem(
                task = context.task,
                metadata = metadata,
                updateTask = { context.updateTask(it) }
            )
        else ->
            throw NotImplementedError("Unknown metadata type $metadata")
    }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeAbleState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(fraction = 0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(taskColor, shape = RoundedCornerShape(CornerSize(9.dp))),
        ) {
        Column(
            modifier = Modifier
                .width(SWIPE_SQUARE_SIZE)
                .height(LocalDensity.current.run { cardHeight.value.toDp() }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                TaskItemSwipeMenu(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    isVisible = swipeAbleState.currentValue == 1,
                    isDone = context.task.isDone(),
                    onDone = {
                        val markedAsDone = if (context.task.isDone()) MarkedAs.UNFINISHED else MarkedAs.DONE

                        context.task
                            .copy(
                                doneDate = if (context.task.isDone()) null else System.currentTimeMillis(),
                                doneCount = context.task.doneCount + markedAsDone.ordinal
                            )
                            .let { taskItemCard.onDone(it, markedAsDone) }
                            .apply { context.updateTask(this) }

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
                .background(MaterialTheme.colors.surface),
            content = {
                val openSubtaskManagerDialog = remember { mutableStateOf(false)  }
                val openTaskEditorDialog = remember { mutableStateOf(false)  }

                TaskCard(
                    task = context.task,
                    updateTask = context.updateTask,
                    deleteTask = context.deleteTask,
                    editTask = { openTaskEditorDialog.value = true },
                    openSubtasksManager = { openSubtaskManagerDialog.value = true },
                    content = { taskItemCard.content() },
                )

                if (openSubtaskManagerDialog.value) {
                    SubtaskManagerDialog(
                        close = { openSubtaskManagerDialog.value = false },
                        updateTask= { context.updateTask(it) },
                        task = context.task
                    )
                }

                if (openTaskEditorDialog.value) {
                    TaskEditorDialog(
                        closeDialog = { openTaskEditorDialog.value = false },
                        saveTask = { context.updateTask(it) },
                        taskToEdit = context.task
                    )
                }
            }
        )
    }
}

@Composable
fun TaskItemSwipeMenu(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isDone: Boolean,
    onDone: () -> Unit,
) {
    val iconId =
        if (isDone)
            R.drawable.ic_baseline_replay_24
        else
            R.drawable.ic_baseline_check_24

    Box(modifier) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "Done",
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .fillMaxSize()
                .clickable {
                    if (isVisible) {
                        onDone()
                    }
                },
        )
    }
}