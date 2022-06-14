package net.dzikoysk.presenceofmind.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.dzikoysk.presenceofmind.task.Task

@Composable
fun SubTaskList(
    task: Task<*>,
    updateTask: (Task<*>) -> Unit
) {
    Box {
        task.subtasks.forEachIndexed { idx, subtask ->
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, top = (30 * idx).dp)
                    .scale(0.85f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Checkbox(
                        checked = subtask.done,
                        onCheckedChange = {
                            subtask.done = !subtask.done
                            updateTask(task.copy(subtasks = task.subtasks.toList()))
                        }
                    )
                    Text(
                        text = subtask.description,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 13.dp)
                    )
                }
            }
        }
    }
}