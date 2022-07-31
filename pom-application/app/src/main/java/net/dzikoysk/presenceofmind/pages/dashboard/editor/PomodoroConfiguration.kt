package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.NumberField
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.UpdateTask
import net.dzikoysk.presenceofmind.model.task.attributes.PomodoroAttribute

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
    Box(Modifier.padding(vertical = 10.dp, horizontal = 8.dp)) {
        NumberField(
            description = "Expected attention in minutes",
            value = pomodoroAttribute.expectedAttentionInMinutes,
            onValueChange = {
                updateTask(
                    task.copy(
                        pomodoroAttribute = pomodoroAttribute.copy(
                            expectedAttentionInMinutes = it
                        )
                    )
                )
            }
        )
    }
}