package net.dzikoysk.presenceofmind.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task

@Composable
fun SubTaskList(
    task: Task,
    updateTask: (Task) -> Unit
) {
    val fontSize = task.description.scaledFontSize()

    Box(modifier = Modifier.padding(start = 3.dp)) {
        task.subtasks.forEachIndexed { idx, subtask ->
            Row(
                modifier = Modifier.padding(top = (30 * idx).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Checkbox(
                        checked = subtask.done,
                        onCheckedChange = {
                            subtask.done = !subtask.done
                            updateTask(task.copy(subtasks = task.subtasks.toList()))
                        },
                        modifier = Modifier.scale(0.8f * (fontSize.value / 12f))
                    )
                    DescriptionMarkdown(
                        description = subtask.description,
                        fontSize = fontSize,
                        modifier = Modifier.padding(top = 15.dp)
                    )
                }
            }
        }
    }
}