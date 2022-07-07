package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.shared.scaledSp
import net.dzikoysk.presenceofmind.task.EventAttribute
import net.dzikoysk.presenceofmind.task.SubTask
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.SubTask
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubtaskManagerDialog(
    close: () -> Unit,
    updateTask: (Task) -> Unit,
    task: Task
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
    updateTask: (Task) -> Unit,
    task: Task
) {
    val subtasks = remember { mutableStateOf(task.subtasks) }

    val subtasksState = rememberReorderableLazyListState(
        onMove = { from, to ->
            subtasks.value = subtasks.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    Box(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.surface),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "Manage subtasks",
                fontSize = 15.scaledSp(),
                fontWeight = FontWeight.Bold,
            )

            LazyColumn(
                state = subtasksState.listState,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .reorderable(subtasksState)
                    .heightIn(0.dp, (LocalConfiguration.current.screenHeightDp / 2).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.background),
            ) {
                items(
                    items = subtasks.value,
                    key = { it.id }
                ) { subtask ->
                    val description = remember { mutableStateOf(subtask.description) }

                    ReorderableItem(
                        state = subtasksState,
                        key = subtask.id
                    ) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)

                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .shadow(elevation.value)
                                .detectReorder(subtasksState),
                            elevation = 0.dp,
                            shape = RoundedCornerShape(CornerSize(12.dp)),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.surface)
                            ) {
                                Icon(
                                    contentDescription = "Move subtask",
                                    painter = painterResource(id = R.drawable.ic_baseline_drag_indicator_24),
                                    modifier = Modifier
                                        //.weight(2f)
                                        .size(24.dp)
                                )
                                OutlinedTextField(
                                    value = description.value,
                                    onValueChange = {
                                        subtask.description = it
                                        description.value = it
                                    },
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .height(48.dp)
                                        .fillMaxWidth(0.75f)
                                )
                                Icon(
                                    contentDescription = "Delete subtask",
                                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                                    modifier = Modifier
                                        //.weight(2f)
                                        .size(24.dp)
                                        .clickable {
                                            subtasks.value = subtasks.value
                                                .toMutableList()
                                                .also { it.remove(subtask) }
                                        }
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
                        .also { it.add(SubTask()) }
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
            metadata = EventAttribute(),
            description = "Preview of one-time task with subtasks",
            subtasks = listOf(
                SubTask(
                    description = "Subtask 1",
                    done = true
                ),
                SubTask(
                    description = "Subtask 2",
                    done = true
                ),
                SubTask(
                    description = "Subtask 2",
                    done = false
                )
            )
        )
    )
}