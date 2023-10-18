package net.dzikoysk.presenceofmind.model.task

import androidx.compose.ui.graphics.Color
import net.dzikoysk.presenceofmind.model.task.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.PomodoroAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.RepetitiveAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventAttribute
import java.io.Serializable
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val categories: List<UUID> = emptyList(),
    val doneDate: Long? = null,
    val doneCount: Int = 0,
    val open: Boolean = true,
    /* Attributes */
    val checklistAttribute: ChecklistAttribute? = null,
    val eventAttribute: EventAttribute? = null,
    val repetitiveAttribute: RepetitiveAttribute? = null,
    val pomodoroAttribute: PomodoroAttribute? = null,
) : Serializable

val Task.attributes: Collection<Attribute>
    get() = listOfNotNull(
        checklistAttribute,
        eventAttribute,
        repetitiveAttribute,
        pomodoroAttribute
    )

fun Task.getAccentColor(): Color =
    attributes.asSequence()
        .sortedBy { it.getPriority() }
        .firstNotNullOfOrNull { it.getDefaultAccentColor() }
        ?: DEFAULT_COLOR

fun Task.isConcealable(): Boolean =
    attributes.any { it.isConcealable() }

fun Task.isOpen(): Boolean =
    open || attributes.find { it.isRunning() } != null

fun Task.isDone(): Boolean =
    doneDate != null
