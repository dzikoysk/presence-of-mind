package net.dzikoysk.presenceofmind.task

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.UUID

data class Task<M : OccurrenceMetadata>(
    val id: UUID = UUID.randomUUID(),
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "metadata-type"
    )
    @JsonSubTypes(value = [
        JsonSubTypes.Type(value = OneTimeMetadata::class, name = "one-time"),
        JsonSubTypes.Type(value = RepetitiveMetadata::class, name = "repetitive"),
        JsonSubTypes.Type(value = LongTermMetadata::class, name = "long-term"),
    ])
    val metadata: M,
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

data class CreateTaskRequest(
    val description: String,
    val policy: OccurrenceMetadata,
    val subtasks: List<SubTask> = emptyList()
)