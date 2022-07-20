package net.dzikoysk.presenceofmind.model.task.attributes.reminder

import net.dzikoysk.presenceofmind.model.task.Attribute
import net.dzikoysk.presenceofmind.model.task.Priority

data class ReminderAttribute(
    val beforeInMillis: Long
) : Attribute {

    override fun getPriority(): Priority =
        Integer.MAX_VALUE

    override fun getName(): String =
        "Reminder"

}