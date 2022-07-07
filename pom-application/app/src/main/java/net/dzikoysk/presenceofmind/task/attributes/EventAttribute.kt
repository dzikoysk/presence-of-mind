package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/** One-time events */
data class EventAttribute(
    val eventDate: EventDateTime = EventDateTime.now()
) : Attribute {

    override fun getDefaultAccentColor(): Color =
        Color(0xFFFFCCF8)

    override fun getName(): String =
        "Event"

}

data class EventDateTime(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int,
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

fun EventDateTime.toLocalDateTime(): Instant =
    LocalDateTime.of(year, month, day, hour, minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()
