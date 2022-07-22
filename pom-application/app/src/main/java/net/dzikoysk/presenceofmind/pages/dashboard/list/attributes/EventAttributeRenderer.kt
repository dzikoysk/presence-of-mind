package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.model.task.MarkedAs
import net.dzikoysk.presenceofmind.model.task.attributes.date.EVENT_DATE_FORMATTER
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventDateTime
import net.dzikoysk.presenceofmind.model.task.attributes.date.getHumanReadableInterval
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import net.dzikoysk.presenceofmind.pages.dashboard.list.SubscribeToOnDone
import net.dzikoysk.presenceofmind.shared.OUTDATED
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Preview(showBackground = true)
@Composable
fun EventAttributeRendererPreview() {
    EventAttributeRenderer(
        eventAttribute = EventAttribute(
            EventDateTime(
                year = 2022,
                month = 12,
                day = 24,
                hour = 17,
                minute = 30
            )
        ),
        subscribeToOnDone = { }
    )
}

@Composable
fun EventAttributeRenderer(
    eventAttribute: EventAttribute,
    subscribeToOnDone: SubscribeToOnDone
) {
    subscribeToOnDone { updatedTask, markedAs ->
        when (markedAs) {
            MarkedAs.UNFINISHED -> updatedTask
            MarkedAs.DONE ->
                updatedTask.copy(
                    eventAttribute = eventAttribute.copy(
                        reminder = eventAttribute.reminder?.copy(
                            scheduledAt = null
                        )
                    )
                )
        }
    }

    var countdownWatcher by remember { mutableStateOf(0) }

    val interval = countdownWatcher.run {
        eventAttribute.getHumanReadableInterval()
            .takeUnless { it == OUTDATED }
            ?.let { "in $it" }
            ?: OUTDATED.lowercase()
    }

    LaunchedEffect(Unit) {
        while(true) {
            countdownWatcher++
            delay(1.seconds)
        }
    }

    val eventDateTime = eventAttribute.eventDate.toLocalDateTime()

    Column(
        Modifier
            .padding(start = 16.dp)
            .padding(top = 2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${EVENT_DATE_FORMATTER.format(eventDateTime)}, $interval",
                fontSize = 10.scaledSp(),
                color = Color.Gray
            )
            if (eventAttribute.reminder != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_notifications_active_24),
                    contentDescription = "Reminder is active",
                    tint = when (eventAttribute.reminder.scheduledAt?.isAfter(Instant.now())) {
                        true -> Color(0xffdaa520)
                        else -> Color.Gray
                    },
                    modifier = Modifier
                        .size(18.dp)
                        .padding(top = 1.dp)
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}