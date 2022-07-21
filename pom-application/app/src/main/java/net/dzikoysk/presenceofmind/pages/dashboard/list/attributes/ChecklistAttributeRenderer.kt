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
import net.dzikoysk.presenceofmind.model.task.MarkedAs
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.UpdateTask
import net.dzikoysk.presenceofmind.model.task.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.ChecklistEntry
import net.dzikoysk.presenceofmind.model.task.attributes.withUpdatedEntry
import net.dzikoysk.presenceofmind.pages.dashboard.list.SubscribeToOnDone

@Composable
fun ChecklistAttributeRenderer(
    task: Task,
    checklistAttribute: ChecklistAttribute,
    subscribeToOnDone: SubscribeToOnDone,
    updateTask: UpdateTask
) {
    val fontSize = task.description.scaledFontSize()

    subscribeToOnDone { updatedTask, markedAs ->
        updatedTask.copy(
            checklistAttribute = checklistAttribute.copy(
                list = checklistAttribute.list.map {
                    it.copy(done = when (markedAs) {
                        MarkedAs.UNFINISHED -> false
                        MarkedAs.DONE -> true
                    })
                }
            )
        )
    }

    Box(modifier = Modifier.padding(start = 3.dp)) {
        checklistAttribute.list
            .sortedBy { it.done }
            .forEachIndexed { idx, subtask ->
                Row(
                    modifier = Modifier.padding(top = (30 * idx).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ChecklistEntryRenderer(
                        task = task,
                        checklistAttribute = checklistAttribute,
                        entry = subtask,
                        fontSize = fontSize,
                        updateTask = updateTask
                    )
                }
            }
    }
}

@Composable
fun ChecklistEntryRenderer(
    task: Task,
    checklistAttribute: ChecklistAttribute,
    entry: ChecklistEntry,
    fontSize: TextUnit,
    updateTask: UpdateTask
) {
    Row {
        Checkbox(
            checked = entry.done,
            onCheckedChange = {
                updateTask(task.copy(
                    checklistAttribute = checklistAttribute.withUpdatedEntry(
                        entry = entry.copy(
                            done = !entry.done
                        )
                    )
                ))
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