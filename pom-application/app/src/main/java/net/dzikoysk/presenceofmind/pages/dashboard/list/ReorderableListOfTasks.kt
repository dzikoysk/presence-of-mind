package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.TaskService
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.util.UUID

@Composable
fun ReorderableListOfTasks(
    taskService: TaskService,
    indexOfTask: (UUID) -> Int,
    tasks: List<Task>,
    openTaskEditor: (Task) -> Unit,
) {
    val listOrderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            taskService.moveTasks(
                from = indexOfTask(from.key as UUID),
                to = indexOfTask(to.key as UUID)
            )
        },
        onDragEnd = { _, _ ->
            taskService.forceSave(sync = false)
        }
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

                    TaskCard(
                        state = listOrderState,
                        context = TaskCardContext(
                            task = task,
                            openTaskEditor = { openTaskEditor(task) },
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
