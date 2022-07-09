package net.dzikoysk.presenceofmind.task

import androidx.compose.ui.graphics.Color
import net.dzikoysk.presenceofmind.task.attributes.*
import java.io.Serializable
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val categories: List<String> = emptyList(),
    val doneDate: Long? = null,
    val doneCount: Int = 0,
    /* Attributes */
    val checklistAttribute: ChecklistAttribute? = null,
    val eventAttribute: EventAttribute? = null,
    val intervalAttribute: IntervalAttribute? = null,
    val pomodoroAttribute: PomodoroAttribute? = null,
) : Serializable

val Task.attributes: Collection<Attribute>
    get() = listOfNotNull(
        checklistAttribute,
        eventAttribute,
        intervalAttribute,
        pomodoroAttribute
    )

inline fun <reified A : Attribute> Task.getAttribute(): A? =
    attributes.find { it is A } as A?

fun Task.getAccentColor(): Color =
    attributes
        .firstNotNullOfOrNull { it.getDefaultAccentColor() }
        ?: Color(0xFFC3EEFF)

fun Task.isOpen(): Boolean =
    attributes.find { it.isRunning() } != null

fun Task.isDone(): Boolean =
    doneDate != null
