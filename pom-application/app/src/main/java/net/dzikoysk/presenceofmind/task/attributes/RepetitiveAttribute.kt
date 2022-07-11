package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color

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

enum class DayOfWeek(val abbreviation: String) {
    MONDAY("M"),
    TUESDAY("Tu"),
    WEDNESDAY("W"),
    THURSDAY("Th"),
    FRIDAY("F"),
    SATURDAY("Sa"),
    SUNDAY("Su")
}



