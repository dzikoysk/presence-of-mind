package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.SwipeState
import net.dzikoysk.presenceofmind.components.SwipeableCard
import net.dzikoysk.presenceofmind.components.drawVerticalScrollbar
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.data.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.data.attributes.ChecklistEntry
import net.dzikoysk.presenceofmind.data.attributes.withUpdatedEntry
import net.dzikoysk.presenceofmind.data.task.Task
import net.dzikoysk.presenceofmind.data.task.UpdateTask
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Preview(showBackground = true)
@Composable
fun ChecklistEditorPreview() {
    ChecklistEditor(
        task = Task(
            description = "Some task",
            checklistAttribute = ChecklistAttribute(
                list = listOf(
                    ChecklistEntry(description = "Done", done = true),
                    ChecklistEntry(description = "Waiting", done = false)
                )
            )
        ),
        updateTask = {},
        close = {}
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChecklistEditor(
    task: Task,
    updateTask: UpdateTask,
    close: () -> Unit
) {
    val checklistAttribute = task.checklistAttribute ?: ChecklistAttribute()
    val subtasks = remember { mutableStateOf(checklistAttribute.list) }

    val updateSubtask: (ChecklistEntry) -> Unit = {
        subtasks.value = checklistAttribute
            .copy(list = subtasks.value)
            .withUpdatedEntry(it)
            .list
    }

    val subtasksState = rememberReorderableLazyListState(
        onMove = { from, to ->
            subtasks.value = subtasks.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    Column(Modifier.padding(horizontal = 24.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .clickable {
                    updateTask(
                        task.copy(
                            checklistAttribute = checklistAttribute.copy(
                                list = subtasks.value
                                    .filter { it.description.trim().isNotEmpty() }
                            )
                        )
                    )
                    close()
                }
        ) {
            Icon(
                contentDescription = "Back to main menu",
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Go back to task editor")
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Manage subtasks",
                fontSize = 12.scaledSp(),
                fontWeight = FontWeight.Bold,
            )
            Icon(
                contentDescription = "Add new subtask",
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable { updateSubtask(ChecklistEntry()) }
            )
        }

        LazyColumn(
            state = subtasksState.listState,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .reorderable(subtasksState)
                .drawVerticalScrollbar(subtasksState.listState)
                // .heightIn(0.dp, (LocalConfiguration.current.screenHeightDp / 2).dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            items(
                items = subtasks.value,
                key = { it.id }
            ) { subtask ->
                ReorderableItem(
                    state = subtasksState,
                    key = subtask.id
                ) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                    val focusRequester = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current

                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(elevation.value)
                            .detectReorder(subtasksState),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(CornerSize(2.dp)),
                    ) {
                        SwipeableCard(
                            swipeThreshold = 0.7f,
                            menuBackgroundColor = Color.LightGray,
                            onStateChange = { swipeContext ->
                                when (swipeContext.currentState()) {
                                    SwipeState.LEFT -> {}
                                    SwipeState.CONTENT -> {}
                                    SwipeState.RIGHT -> {
                                        subtasks.value = subtasks.value
                                            .toMutableList()
                                            .also { it.remove(subtask) }
                                    }
                                }
                            },
                            content = { _, _ ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colors.background)
                                        .padding(horizontal = 5.dp, vertical = 10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = subtask.description,
                                        onValueChange = { updateSubtask(subtask.copy(description = it)) },
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                when {
                                                    subtask.description.trim().isEmpty() -> keyboardController?.hide()
                                                    else -> updateSubtask(ChecklistEntry())
                                                }
                                            }
                                        ),
                                        modifier = Modifier
                                            .height(48.dp)
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colors.background)
                                            .focusRequester(focusRequester)
                                    )
                                }
                            },
                            rightContent = { _, _ ->
                                Icon(
                                    contentDescription = "Delete subtask",
                                    painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                    }

                    if (subtask.description.isEmpty()) {
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    }
                }
            }
        }
    }
}

