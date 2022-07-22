package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.isConcealable
import net.dzikoysk.presenceofmind.model.task.isOpen
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.ChecklistAttributeRenderer
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.DescriptionMarkdown
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.EventAttributeRenderer
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.PomodoroAttributeRenderer
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.RepetitiveAttributeRenderer

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                DescriptionMarkdown(
                    modifier = Modifier.padding(start = 16.dp),
                    description = task.description
                )
                if (task.isConcealable()) {
                    Box(Modifier.padding(start = 1.dp)) {
                        Icon(
                            contentDescription = "Hide attributes",
                            painter = when (task.isOpen()) {
                                true -> painterResource(id = R.drawable.ic_baseline_arrow_drop_up_24)
                                false -> painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24)
                            },
                            modifier = Modifier.clickable {
                                updateTask(
                                    task.copy(
                                        open = !task.open
                                    )
                                )
                            }
                        )
                    }
                }
            }

            task.eventAttribute?.also { eventAttribute ->
                EventAttributeRenderer(
                    eventAttribute = eventAttribute,
                    subscribeToOnDone = subscribeToOnDone
                )
            }
            task.repetitiveAttribute?.also { intervalAttribute ->
                RepetitiveAttributeRenderer(
                    repetitiveAttribute = intervalAttribute,
                )
            }

            if (task.isOpen()) {
                task.pomodoroAttribute?.also {
                    PomodoroAttributeRenderer(
                        task = task,
                        subscribeToOnDone = subscribeToOnDone,
                        updateTask = updateTask
                    )
                }
                task.checklistAttribute?.also { checklistAttribute ->
                    ChecklistAttributeRenderer(
                        task = task,
                        checklistAttribute = checklistAttribute,
                        subscribeToOnDone = subscribeToOnDone,
                        updateTask = { updateTask(it) }
                    )
                }
            }
        }

    }
}
