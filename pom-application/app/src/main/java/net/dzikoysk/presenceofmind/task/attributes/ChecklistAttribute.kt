package net.dzikoysk.presenceofmind.task.attributes

import java.util.UUID

data class ChecklistAttribute(
    val list: List<ChecklistEntry> = emptyList()
) : Attribute {

    override fun getName(): String =
        "Checklist"

}

data class ChecklistEntry(
    val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var done: Boolean = false
)
