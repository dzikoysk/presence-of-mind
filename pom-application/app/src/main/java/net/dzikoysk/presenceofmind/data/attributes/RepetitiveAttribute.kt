package net.dzikoysk.presenceofmind.data.attributes

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

/**
 * Repeat task by:
 * - Interval in hours
 * - Day of week
 */
data class RepetitiveAttribute(
    val intervalInDays: Int? = null,
    val daysOfWeek: Collection<DayOfWeek>? = null
) : Attribute {

    override fun getPriority(): Int =
        1

    override fun getDefaultAccentColor(): Color =
        Color(0xFFBBF0B1)

    override fun getName(): String =
        "Interval"

}

enum class RepetitiveVariant(val description: String) {
    DAYS_OF_WEEK("Days of week"),
    INTERVAL_IN_DAYS("Interval in days")
}

/** Fixed size abbreviations */
fun getShortAbbreviation(dayOfWeek: DayOfWeek): String =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Mo"
        DayOfWeek.TUESDAY -> "Tu"
        DayOfWeek.WEDNESDAY -> "We"
        DayOfWeek.THURSDAY -> "Th"
        DayOfWeek.FRIDAY -> "Fr"
        DayOfWeek.SATURDAY -> "Sa"
        DayOfWeek.SUNDAY -> "Su"
    }
