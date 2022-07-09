package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.UpdateTask
import net.dzikoysk.presenceofmind.task.attributes.PomodoroAttribute

@Preview(showBackground = true)
@Composable
fun PomodoroConfigurationPreview() {
    PomodoroConfiguration(
        task = Task(),
        pomodoroAttribute = PomodoroAttribute(90),
        updateTask = {}
    )
}

@Composable
fun PomodoroConfiguration(
    task: Task,
    pomodoroAttribute: PomodoroAttribute,
    updateTask: UpdateTask
) {
    Column(Modifier.padding(12.dp)) {
        OutlinedTextField(
            value = pomodoroAttribute.expectedAttentionInMinutes.takeIf { it != 0 }?.toString() ?: "",
            label = { Text("Expected attention in minutes") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(58.dp),
            onValueChange = {
                updateTask(task.copy(
                    pomodoroAttribute = pomodoroAttribute.copy(
                        expectedAttentionInMinutes = it.toIntOrNull() ?: 0
                    )
                ))
            }
        )
    }
}