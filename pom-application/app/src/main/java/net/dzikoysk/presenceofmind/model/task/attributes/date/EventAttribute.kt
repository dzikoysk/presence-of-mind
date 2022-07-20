package net.dzikoysk.presenceofmind.model.task.attributes.date

import androidx.compose.ui.graphics.Color
import net.dzikoysk.presenceofmind.model.task.Attribute
import net.dzikoysk.presenceofmind.model.task.Priority
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/** One-time events */
data class EventAttribute(
    val eventDate: EventDateTime = EventDateTime.now()
) : Attribute {

    override fun getPriority(): Priority =
        2

    override fun getDefaultAccentColor(): Color =
        Color(0xFFFFCCF8)

    override fun getName(): String =
        "Event"

}

data class EventDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
) {

    companion object {
        fun now(): EventDateTime =
            Instant.now().atZone(ZoneId.systemDefault()).run {
                EventDateTime(
                    year = year,
                    month = monthValue,
                    day = dayOfMonth,
                    hour = hour,
                    minute = minute
                )
            }
    }

}

private fun Int.getPrettyNumber(): String =
    String.format("%02d", this)

fun EventDateTime.getDate(): Triple<Int, Int, Int> =
    Triple(year, month, day)

fun EventDateTime.getDateAsString(): String =
    "$year-${(month + 1).getPrettyNumber()}-${day.getPrettyNumber()}"

fun EventDateTime.getTime(): Pair<Int, Int> =
    hour to minute

fun EventDateTime.getTimeAsString(): String =
    "${hour.getPrettyNumber()}:${minute.getPrettyNumber()}"

fun EventDateTime.toLocalDateTime(): ZonedDateTime =
    LocalDateTime.of(year, month, day, hour, minute)
        .atZone(ZoneId.systemDefault())
