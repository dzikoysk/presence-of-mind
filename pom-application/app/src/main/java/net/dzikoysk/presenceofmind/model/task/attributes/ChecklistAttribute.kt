package net.dzikoysk.presenceofmind.model.task.attributes

import androidx.annotation.CheckResult
import androidx.compose.ui.graphics.Color
import net.dzikoysk.presenceofmind.model.task.Attribute
import java.util.UUID

data class ChecklistAttribute(
    val list: List<ChecklistEntry> = emptyList()
) : Attribute {

    override fun isConcealable(): Boolean =
        true

    override fun getPriority(): Int =
        4

    override fun getDefaultAccentColor(): Color =
        Color(0xFFFFDD78)

    override fun getName(): String =
        "Checklist"

}

data class ChecklistEntry(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val done: Boolean = false
)

@CheckResult
fun ChecklistAttribute.withUpdatedEntry(entry: ChecklistEntry): ChecklistAttribute =
    copy(list = list.updateEntry(entry))

private fun List<ChecklistEntry>.updateEntry(entry: ChecklistEntry): List<ChecklistEntry> =
    toMutableList().also {
        when (val id = it.indexOfFirst { element -> element.id == entry.id }) {
            -1 -> it.add(entry)
            else -> it[id] = entry
        }
    }
