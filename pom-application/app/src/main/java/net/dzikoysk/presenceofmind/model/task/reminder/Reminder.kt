package net.dzikoysk.presenceofmind.model.task.reminder

import java.time.Instant

data class Reminder(
    val beforeInMinutes: Int = 5,
    val ring: Boolean = false,
    val scheduledAt: Instant? = null
)

fun Reminder.hasOutdatedSchedule(now: Instant): Boolean =
    scheduledAt?.isBefore(now) ?: false