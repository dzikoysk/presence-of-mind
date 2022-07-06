package net.dzikoysk.presenceofmind.task.attributes

import java.util.UUID

data class SubtasksAttribute(
    val subtasks: List<SubTask> = emptyList()
)

data class SubTask(
    val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var done: Boolean = false
)
