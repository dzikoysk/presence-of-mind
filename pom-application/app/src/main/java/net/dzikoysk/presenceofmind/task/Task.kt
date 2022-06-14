package net.dzikoysk.presenceofmind.task

import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val metadata: OccurrenceMetadata,
    val description: String = "",
    val done: Boolean = false,
    val doneCount: Int = 0,
    val subtasks: List<SubTask> = emptyList()
)

data class SubTask(
    val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var done: Boolean = false
)
