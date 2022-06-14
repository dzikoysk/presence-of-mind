package net.dzikoysk.presenceofmind.task

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val metadata: OccurrenceMetadata,
    val description: String = "",
    val doneDate: Long? = null,
    val doneCount: Int = 0,
    val subtasks: List<SubTask> = emptyList()
) {

    @JsonIgnore
    fun isDone(): Boolean =
        doneDate != null

}

data class SubTask(
    val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var done: Boolean = false
)
