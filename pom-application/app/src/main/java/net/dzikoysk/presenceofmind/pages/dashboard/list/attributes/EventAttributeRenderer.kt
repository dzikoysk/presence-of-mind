package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.data.attributes.EventAttribute
import net.dzikoysk.presenceofmind.data.attributes.EventDateTime
import net.dzikoysk.presenceofmind.data.attributes.toLocalDateTime
import net.dzikoysk.presenceofmind.shared.incomingDurationToHumanReadableFormat
import net.dzikoysk.presenceofmind.shared.plural
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.time.toKotlinDuration

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

    val interval = Duration.between(Instant.now(), eventDateTime)
        .toKotlinDuration()
        .let {
            when (it.inWholeDays) {
                0L -> it.incomingDurationToHumanReadableFormat()
                else -> plural(it.inWholeDays, "day")
            }
        }

    Column(Modifier.padding(start = 16.dp)) {
        Text(
            text = "${eventDateFormatter.format(eventDateTime)}, in $interval",
            fontSize = 10.scaledSp(),
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}