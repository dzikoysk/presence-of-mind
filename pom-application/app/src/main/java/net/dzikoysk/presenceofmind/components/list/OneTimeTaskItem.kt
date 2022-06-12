package net.dzikoysk.presenceofmind.components.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.dzikoysk.presenceofmind.shared.incomingDurationToHumanReadableFormat
import net.dzikoysk.presenceofmind.task.OneTimeMetadata
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.toLocalDateTime
import java.time.Duration
import java.time.Instant
import kotlin.time.toKotlinDuration

@Composable
fun createOneTimeTaskItem(task: Task<OneTimeMetadata>): TaskItemCard<OneTimeMetadata> {
    return TaskItemCard(
        content = {
            Column(Modifier.padding(start = 16.dp)) {
                Text(
                    text = task.description,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = Duration.between(Instant.now(), task.metadata.eventDate.toLocalDateTime())
                        .toKotlinDuration()
                        .incomingDurationToHumanReadableFormat(),
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    )
}