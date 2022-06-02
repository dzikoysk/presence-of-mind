package net.dzikoysk.presenceofmind.shared

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

fun Duration.incomingDurationToHumanReadableFormat(): String {
    val parts = ArrayList<String>()

    if (inWholeDays > 0) {
        parts.add(plural(inWholeDays, "day"))
    }

    val inHours = inWholeHours - (inWholeDays * 24)

    if (inHours > 0) {
        parts.add(plural(inHours, "hour"))
    }

    val inMinutes = inWholeMinutes - (inWholeHours * 60)

    if (inMinutes > 0) {
        parts.add(plural(inMinutes, "minute"))
    }

    return parts.joinToString(separator = " ")
        .takeIf { it.trim().isNotEmpty() }
        ?: "Outdated"
}

@ExperimentalTime
fun Duration.timeToHumanReadableFormat(): String {
    val parts = ArrayList<String>()

    if (inWholeHours > 0) {
        parts.add(inWholeHours.toString() + "h")
    }

    val inMinutes = inWholeMinutes - (inWholeHours * 60)

    if (inMinutes > 0) {
        parts.add(inMinutes.toString() + "m")
    }

    val inSeconds = inWholeSeconds - (inWholeMinutes * 60)
    parts.add(inSeconds.toString() + "s")

    return parts.joinToString(separator = " ")
}

fun plural(num: Long, unit: String): String =
    num.toString() + " " + unit + (if (num == 1L) "" else "s")