package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.*
import net.dzikoysk.presenceofmind.task.Task

@Composable
fun TaskCardContent(
    task: Task,
    subscribeToOnDone: SubscribeToOnDone,
    updateTask: (Task) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Column {
            DescriptionMarkdown(
                modifier = Modifier.padding(start = 16.dp),
                description = task.description
            )
            task.eventAttribute?.also { eventAttribute ->
                EventAttributeRenderer(
                    task = task,
                    eventAttribute = eventAttribute
                )
            }
            task.intervalAttribute?.also { intervalAttribute ->
                IntervalAttributeRenderer(
                    intervalAttribute = intervalAttribute,
                )
            }
            task.pomodoroAttribute?.also {
                PomodoroAttributeRenderer(
                    task = task,
                    subscribeToOnDone = subscribeToOnDone,
                    updateTask = updateTask
                )
            }
            ChecklistAttributeRenderer(
                task = task,
                updateTask = { updateTask(it) }
            )
        }
    }
}
