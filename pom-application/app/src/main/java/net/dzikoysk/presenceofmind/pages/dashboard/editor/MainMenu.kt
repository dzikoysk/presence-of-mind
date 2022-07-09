package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.shared.mirror.drawVerticalScrollbar
import net.dzikoysk.presenceofmind.shared.scaledSp
import net.dzikoysk.presenceofmind.task.SaveTask
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.UpdateTask
import net.dzikoysk.presenceofmind.task.attributes.*

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
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .drawVerticalScrollbar(state),
        state = state
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
                onEdit = { selectTab(EditorTab.CHECKLIST) },
                onDisable = { updateTask(task.copy(checklistAttribute = null)) }
            )
        }

        item {
            AttributeSetup(
                attribute = task.eventAttribute,
                attributeDefaultInstance = EventAttribute(),
                attributeName = "event",
                onEnable = { updateTask(task.copy(eventAttribute = EventAttribute())) },
                onEdit = {  },
                onDisable = { updateTask(task.copy(eventAttribute = null)) }
            )
        }

        item {
            AttributeSetup(
                attribute = task.pomodoroAttribute,
                attributeName = "pomodoro",
                attributeDefaultInstance = PomodoroAttribute(),
                onEnable = { updateTask(task.copy(pomodoroAttribute = PomodoroAttribute())) },
                onEdit = { },
                onDisable = { updateTask(task.copy(pomodoroAttribute = null)) }
            )
        }

        item {
            AttributeSetup(
                attribute = task.intervalAttribute,
                attributeDefaultInstance = IntervalAttribute(),
                attributeName = "interval",
                onEnable = { updateTask(task.copy(intervalAttribute = IntervalAttribute())) },
                onEdit = { },
                onDisable = { updateTask(task.copy(intervalAttribute = null)) }
            ) {
                IntervalConfiguration(
                    task = task,
                    intervalAttribute = it,
                    updateTask = updateTask
                )
            }
        }

        item {
            Button(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    saveTask(task)
                    close()
                },
                content = { Text(text = "Save") }
            )
        }
    }
}

@Composable
fun <A : Attribute> AttributeSetup(
    attribute: A?,
    attributeDefaultInstance: A,
    attributeName: String,
    onEnable: () -> Unit,
    onEdit: () -> Unit,
    onDisable: () -> Unit,
    content: @Composable (A) -> Unit = {}
) {
    val sharedButtonModifier = Modifier
        .padding(vertical = 8.dp, horizontal = 10.dp)

    if (attribute == null) {
        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            colors = buttonColors(MaterialTheme.colors.surface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, attributeDefaultInstance.getDefaultAccentColor() ?: MaterialTheme.colors.primary),
            onClick = { onEnable() }
        ) {
            Text(
                text = attributeName.replaceFirstChar { it.uppercase() },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
        }
    } else {
        Box(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                content(attribute)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .then(sharedButtonModifier),
                        colors = buttonColors(backgroundColor = attribute.getDefaultAccentColor() ?: Color.LightGray),
                        onClick = { onEdit() },
                        content = { Text(text = "Modify") }
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth().then(sharedButtonModifier),
                        onClick = { onDisable() },
                        content = { Text(text = "Disable") }
                    )
                }
            }
            Text(
                text = "Configure $attributeName",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .background(MaterialTheme.colors.surface)
            )
        }
    }
}