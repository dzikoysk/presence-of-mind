package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class ChecklistAttribute(
    val list: List<ChecklistEntry> = emptyList()
) : Attribute {

    override fun getPriority(): Int =
        4

    override fun getDefaultAccentColor(): Color =
        Color(0xFFFFDD78)

    override fun getName(): String =
        "Checklist"

}

data class ChecklistEntry(
    val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var done: Boolean = false
)
