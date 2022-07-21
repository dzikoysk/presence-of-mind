package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventDateTime
import net.dzikoysk.presenceofmind.model.task.attributes.date.getHumanReadableInterval
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import net.dzikoysk.presenceofmind.shared.OUTDATED
import java.time.format.DateTimeFormatter
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
        )
    )
}

@Composable
fun EventAttributeRenderer(eventAttribute: EventAttribute) {
    val eventDateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM HH:mm") }
    val eventDateTime = eventAttribute.eventDate.toLocalDateTime()

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

    Column(Modifier.padding(start = 16.dp)) {
        Text(
            text = "${eventDateFormatter.format(eventDateTime)}, $interval",
            fontSize = 10.scaledSp(),
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}