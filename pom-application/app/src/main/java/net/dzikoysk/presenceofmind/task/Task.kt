package net.dzikoysk.presenceofmind.task

import com.fasterxml.jackson.annotation.JsonIgnore
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.IntervalAttribute
import net.dzikoysk.presenceofmind.task.attributes.PomodoroAttribute
import net.dzikoysk.presenceofmind.task.attributes.SubtasksAttribute
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val categories: List<String> = emptyList(),
    val doneDate: Long? = null,
    val doneCount: Int = 0,
    /* Attributes */
    val subtasksAttribute: SubtasksAttribute? = null,
    val eventAttribute: EventAttribute? = null,
    val interval: IntervalAttribute? = null,
    val pomodoro: PomodoroAttribute? = null,
) {

    @JsonIgnore
    fun isDone(): Boolean =
        doneDate != null

}
