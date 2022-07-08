package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.shared.mirror.drawVerticalScrollbar
import net.dzikoysk.presenceofmind.shared.scaledSp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.attributes.ChecklistAttribute

@Composable
fun MainMenu(
    close: () -> Unit,
    task: Task,
    selectTab: (EditorTab) -> Unit,
    updateTask: (Task) -> Unit,
    saveTask: (Task) -> Unit,
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
            val checklistEnabled = remember { mutableStateOf(task.checklistAttribute != null) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checklistEnabled.value,
                        onCheckedChange = {
                            checklistEnabled.value = !checklistEnabled.value
                            updateTask(
                                when (checklistEnabled.value) {
                                    true -> task.copy(checklistAttribute = ChecklistAttribute())
                                    false -> task.copy(checklistAttribute = null)
                                }
                            )
                        }
                    )
                    Text(
                        text = "Enable checklist"
                    )
                }
                task.checklistAttribute?.also {
                    Button(
                        onClick = { selectTab(EditorTab.CHECKLIST) },
                        content = { Text(text = "Edit subtasks -->")}
                    )
                }
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {}
                )
                Text(
                    text = "Add event date"
                )
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {}
                )
                Text(
                    text = "Enable pomodoro"
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