package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.drawVerticalScrollbar
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.model.task.DeleteTask
import net.dzikoysk.presenceofmind.model.task.SaveTask
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.UpdateTask
import net.dzikoysk.presenceofmind.model.task.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.PomodoroAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.RepetitiveAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventDateTime

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        MainMenu(
            close = {},
            selectTab = {},
            isNew = true,
            task = Task(
                description = "Task to edit",
                checklistAttribute = ChecklistAttribute()
            ),
            updateTask = {},
            saveTask = {},
            deleteTask = {}
        )
    }
}

@Composable
fun MainMenu(
    close: () -> Unit,
    isNew: Boolean,
    task: Task,
    selectTab: SelectTab,
    updateTask: UpdateTask,
    saveTask: SaveTask,
    deleteTask: DeleteTask
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .drawVerticalScrollbar(state),
        state = state,
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        item {
            val message = when (isNew) {
                true -> "Add a new task  \uD83C\uDFA8"
                false -> "Modify task  \uD83C\uDFA8"
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 5.dp)
            ) {
                Text(
                    text = message,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.scaledSp()
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                    contentDescription = "Close editor",
                    modifier = Modifier.clickable { close() }
                )
            }
        }

        item {
            OutlinedTextField(
                value = task.description,
                label = { Text("Describe your task") },
                onValueChange = { updateTask(task.copy(description = it)) },
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .height(140.dp)
                    .fillMaxWidth(),
            )
        }

        item {
            AttributeSetup(
                attribute = task.checklistAttribute,
                attributeName = "checklist",
                attributeDefaultInstance = ChecklistAttribute(),
                onEnable = { updateTask(task.copy(checklistAttribute = ChecklistAttribute())) },
                onDisable = { updateTask(task.copy(checklistAttribute = null)) }
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    colors = buttonColors(
                        contentColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    shape = RoundedCornerShape(6.dp),
                    onClick = { selectTab(EditorTab.CHECKLIST) },
                    content = { Text(text = "Modify") }
                )
            }
        }

        item {
            AttributeSetup(
                attribute = task.eventAttribute,
                attributeDefaultInstance = EventAttribute(),
                attributeName = "event",
                onEnable = {
                    updateTask(task.copy(
                        eventAttribute = EventAttribute(
                            eventDate = EventDateTime.now()
                        )
                    ))
                },
                onDisable = { updateTask(task.copy(eventAttribute = null)) }
            ) {
                EventConfiguration(
                    task = task,
                    eventAttribute = it,
                    updateTask = updateTask
                )
            }
        }

        item {
            AttributeSetup(
                attribute = task.repetitiveAttribute,
                attributeDefaultInstance = RepetitiveAttribute(),
                attributeName = "repetitive",
                onEnable = { updateTask(task.copy(repetitiveAttribute = RepetitiveAttribute())) },
                onDisable = { updateTask(task.copy(repetitiveAttribute = null)) }
            ) {
                RepetitiveConfiguration(
                    task = task,
                    repetitiveAttribute = it,
                    updateTask = updateTask
                )
            }
        }

        item {
            AttributeSetup(
                attribute = task.pomodoroAttribute,
                attributeName = "pomodoro",
                attributeDefaultInstance = PomodoroAttribute(),
                onEnable = { updateTask(task.copy(pomodoroAttribute = PomodoroAttribute())) },
                onDisable = { updateTask(task.copy(pomodoroAttribute = null)) }
            ) {
                PomodoroConfiguration(
                    task = task,
                    pomodoroAttribute = it,
                    updateTask = updateTask
                )
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(6.dp),
                    colors = buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    onClick = {
                        saveTask(task)
                        close()
                    },
                    content = { Text(text = "Save") }
                )
                Button(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        deleteTask(task)
                        close()
                    },
                    content = {
                        when (isNew) {
                            true -> Text(text = "Cancel")
                            false -> Text(text = "Delete")
                        }
                    }
                )
            }
        }
    }
}

