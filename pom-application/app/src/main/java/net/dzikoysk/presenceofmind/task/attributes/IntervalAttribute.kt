package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color

data class IntervalAttribute(
    val intervalInDays: Int = 1
) : Attribute {

    override fun getDefaultAccentColor(): Color =
        Color(0xFFBBF0B1)

    override fun getName(): String =
        "Interval"

}



