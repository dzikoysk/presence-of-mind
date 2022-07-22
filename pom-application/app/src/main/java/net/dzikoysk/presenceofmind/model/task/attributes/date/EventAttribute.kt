package net.dzikoysk.presenceofmind.model.task.attributes.date

import androidx.compose.ui.graphics.Color
import net.dzikoysk.presenceofmind.model.task.Attribute
import net.dzikoysk.presenceofmind.model.task.Priority
import net.dzikoysk.presenceofmind.model.task.reminder.Reminder
import net.dzikoysk.presenceofmind.model.task.reminder.hasOutdatedSchedule
import net.dzikoysk.presenceofmind.shared.OUTDATED
import net.dzikoysk.presenceofmind.shared.incomingDurationToHumanReadableFormat
import net.dzikoysk.presenceofmind.shared.plural
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.toKotlinDuration

val EVENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM HH:mm")

/** One-time events */
data class EventAttribute(
    val eventDate: EventDateTime = EventDateTime.now(),
    val reminder: Reminder? = null,
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

    constructor(dateTime: ZonedDateTime) : this(
        year = dateTime.year,
        month = dateTime.monthValue,
        day = dateTime.dayOfMonth,
        hour = dateTime.hour,
        minute = dateTime.minute
    )

    companion object {
        fun now(): EventDateTime = EventDateTime(ZonedDateTime.now())
    }

}

fun EventAttribute.getHumanReadableInterval(): String =
    Duration.between(ZonedDateTime.now(), eventDate.toLocalDateTime())
        .toKotlinDuration()
        .takeUnless { it.isNegative() }
        ?.let {
            when (it.inWholeDays) {
                0L -> it.incomingDurationToHumanReadableFormat()
                else -> plural(it.inWholeDays, "day")
            }
        }
        ?: OUTDATED

fun EventAttribute.hasOutdatedSchedule(now: Instant): Boolean =
    reminder?.hasOutdatedSchedule(now) ?: false

fun EventDateTime.toLocalDateTime(): ZonedDateTime =
    LocalDateTime.of(year, month, day, hour, minute).atZone(ZoneId.systemDefault())
