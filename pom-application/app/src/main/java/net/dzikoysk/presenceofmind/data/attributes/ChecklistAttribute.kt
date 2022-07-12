package net.dzikoysk.presenceofmind.data.attributes

import androidx.annotation.CheckResult
import androidx.compose.ui.graphics.Color
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

@CheckResult
fun ChecklistAttribute.withUpdatedEntry(entry: ChecklistEntry): ChecklistAttribute =
    copy(
        list = list
            .indexOfFirst { it.id == entry.id }
            .takeIf { it != -1 }
            ?.let { list.toMutableList().apply { set(it, entry) } }
            ?: list.toMutableList().apply { add(entry) }
    )

data class ChecklistEntry(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val done: Boolean = false
)
