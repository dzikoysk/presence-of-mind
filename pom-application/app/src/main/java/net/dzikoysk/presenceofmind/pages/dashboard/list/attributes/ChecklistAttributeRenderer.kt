package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.attributes.ChecklistEntry

@Composable
fun ChecklistAttributeRenderer(
    task: Task,
    updateTask: (Task) -> Unit
) {
    val fontSize = task.description.scaledFontSize()

    task.checklistAttribute?.list?.also { subtasks ->
        Box(modifier = Modifier.padding(start = 3.dp)) {
            subtasks.forEachIndexed { idx, subtask ->
                Row(
                    modifier = Modifier.padding(top = (30 * idx).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ChecklistEntryRenderer(
                        task = task,
                        entry = subtask,
                        fontSize = fontSize,
                        updateTask = updateTask
                    )
                }
            }
        }
    }
}

@Composable
fun ChecklistEntryRenderer(
    task: Task,
    entry: ChecklistEntry,
    fontSize: TextUnit,
    updateTask: (Task) -> Unit
) {
    Row {
        Checkbox(
            checked = entry.done,
            onCheckedChange = {
                entry.done = !entry.done
                updateTask(task.copy(checklistAttribute = task.checklistAttribute!!.copy()))
            },
            modifier = Modifier.scale(0.8f * (fontSize.value / 12f))
        )
        DescriptionMarkdown(
            description = entry.description,
            fontSize = fontSize,
            modifier = Modifier.padding(top = 15.dp)
        )
    }
}