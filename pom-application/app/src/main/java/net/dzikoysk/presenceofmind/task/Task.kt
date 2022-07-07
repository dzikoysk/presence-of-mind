package net.dzikoysk.presenceofmind.task

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore
import net.dzikoysk.presenceofmind.task.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.IntervalAttribute
import net.dzikoysk.presenceofmind.task.attributes.PomodoroAttribute
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val color: Color = Color.White,
    val categories: List<String> = emptyList(),
    val doneDate: Long? = null,
    val doneCount: Int = 0,
    /* Attributes */
    val checklistAttribute: ChecklistAttribute? = null,
    val eventAttribute: EventAttribute? = null,
    val intervalAttribute: IntervalAttribute? = null,
    val pomodoroAttribute: PomodoroAttribute? = null,
) {

    @JsonIgnore
    fun isOpen(): Boolean =
        pomodoroAttribute?.countdownSession?.isRunning() ?: false

    @JsonIgnore
    fun isDone(): Boolean =
        doneDate != null

}
