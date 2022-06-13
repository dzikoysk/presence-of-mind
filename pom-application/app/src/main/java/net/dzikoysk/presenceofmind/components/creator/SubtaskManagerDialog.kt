package net.dzikoysk.presenceofmind.components.creator

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.task.OneTimeMetadata
import net.dzikoysk.presenceofmind.task.SubTask
import net.dzikoysk.presenceofmind.task.Task
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.util.UUID

@ExperimentalComposeUiApi
@Composable
fun SubtaskManagerDialog(
    close: () -> Unit,
    updateTask: (Task<*>) -> Unit,
    task: Task<*>
) {
    Dialog(
        onDismissRequest = { close() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            SubtaskManager(
                close = close,
                updateTask = updateTask,
                task = task
            )
        }
    )
}

@Composable
fun SubtaskManager(
    close: () -> Unit,
    updateTask: (Task<*>) -> Unit,
    task: Task<*>
) {
    val subtasks = remember { mutableStateOf(task.subtasks.sortedBy { it.index }) }

    val subtasksState = rememberReorderableLazyListState(
        onMove = { from, to ->
            subtasks.value = subtasks.value.toMutableList().apply {
                getOrNull(to.index)?.run { index++ }
                getOrNull(from.index)?.run { index = to.index }
                add(to.index, removeAt(from.index))
            }
        }
    )

    Box(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.background),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "Manage subtasks",
                fontWeight = FontWeight.Bold,
            )

            LazyColumn(
                state = subtasksState.listState,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .reorderable(subtasksState),
            ) {
                items(
                    items = subtasks.value,
                    key = { it.index }
                ) { subtask ->
                    val description = remember { mutableStateOf(subtask.description) }

                    ReorderableItem(
                        state = subtasksState,
                        key = subtask.index
                    ) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)

                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .shadow(elevation.value)
                                .detectReorderAfterLongPress(subtasksState)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    contentDescription = "Move subtask",
                                    painter = painterResource(id = R.drawable.ic_baseline_drag_indicator_24),
                                    modifier = Modifier.weight(2f)
                                )
                                OutlinedTextField(
                                    value = description.value,
                                    onValueChange = {
                                        subtask.description = it
                                        description.value = it
                                    },
                                    modifier = Modifier
                                        .padding(vertical = 6.dp)
                                        .height(50.dp)
                                )
                                Icon(
                                    contentDescription = "Delete subtask",
                                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                                    modifier = Modifier.weight(2f)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    subtasks.value = subtasks.value
                        .toMutableList()
                        .also { it.add(SubTask(index = it.size)) }
                },
                content = {
                    Text("Add a new subtask")
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    updateTask(task.copy(subtasks = subtasks.value))
                    close()
                },
                content = {
                    Text(text = "Save")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubtaskManagerPreview() {
    SubtaskManager(
        close = {},
        updateTask = {},
        task = Task(
            id = UUID.randomUUID(),
            metadata = OneTimeMetadata(),
            description = "Preview of one-time task with subtasks",
            subtasks = listOf(
                SubTask(
                    index = 0,
                    description = "Subtask 1",
                    done = true
                ),
                SubTask(
                    index = 1,
                    description = "Subtask 2",
                    done = true
                ),
                SubTask(
                    index = 2,
                    description = "Subtask 2",
                    done = false
                )
            )
        )
    )
}