package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.pages.dashboard.list.DescriptionMarkdown
import net.dzikoysk.presenceofmind.pages.dashboard.list.TaskItemCard
import net.dzikoysk.presenceofmind.shared.incomingDurationToHumanReadableFormat
import net.dzikoysk.presenceofmind.shared.scaledSp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.toLocalDateTime
import java.time.Duration
import java.time.Instant
import kotlin.time.toKotlinDuration

@Composable
fun createOneTimeTaskItem(
    task: Task,
    eventAttribute: EventAttribute
): TaskItemCard {
    return TaskItemCard(
        content = {
            Column(Modifier.padding(start = 16.dp)) {
                DescriptionMarkdown(
                    description = task.description
                )
                Text(
                    text = Duration.between(Instant.now(), eventAttribute.eventDate.toLocalDateTime())
                        .toKotlinDuration()
                        .incomingDurationToHumanReadableFormat(),
                    fontSize = 10.scaledSp(),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    )
}