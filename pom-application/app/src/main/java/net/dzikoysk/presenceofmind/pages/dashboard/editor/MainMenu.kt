package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.drawVerticalScrollbar
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.task.DeleteTask
import net.dzikoysk.presenceofmind.task.SaveTask
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.UpdateTask
import net.dzikoysk.presenceofmind.task.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.PomodoroAttribute
import net.dzikoysk.presenceofmind.task.attributes.RepetitiveAttribute

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
            Text(
                text = "Add a new task  \uD83C\uDFA8",
                modifier = Modifier.padding(top = 24.dp, bottom = 5.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.scaledSp()
            )
        }

        item {
            OutlinedTextField(
                value = task.description,
                label = { Text("Describe your task") },
                onValueChange = { updateTask(task.copy(description = it)) },
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .height(160.dp)
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
                    colors = buttonColors(backgroundColor = it.getDefaultAccentColor()),
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
                onEnable = { updateTask(task.copy(eventAttribute = EventAttribute())) },
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
                    content = { Text(text = "Delete") }
                )
            }
        }
    }
}

