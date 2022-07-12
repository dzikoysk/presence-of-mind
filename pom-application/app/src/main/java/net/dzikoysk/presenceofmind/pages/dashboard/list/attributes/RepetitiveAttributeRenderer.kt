package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.pages.dashboard.list.TaskCard
import net.dzikoysk.presenceofmind.pages.dashboard.list.TaskCardContext
import net.dzikoysk.presenceofmind.shared.plural
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.attributes.RepetitiveAttribute
import net.dzikoysk.presenceofmind.task.attributes.getShortAbbreviation
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import java.util.UUID

@Preview(showBackground = true)
@Composable
private fun RepetitiveAttributeRendererPreview() {
    TaskCard(
        state = rememberReorderableLazyListState(onMove = { _, _ -> }),
        context = TaskCardContext(
            task = Task(
                id = UUID.randomUUID(),
                description = "Preview of repetitive task item",
                repetitiveAttribute = RepetitiveAttribute(
                    intervalInDays = 3,
                )
            )
        ),
    )
}

@Composable
fun RepetitiveAttributeRenderer(repetitiveAttribute: RepetitiveAttribute) {
    val attributeDescription = when {
        repetitiveAttribute.intervalInDays != null -> "Every ${plural(repetitiveAttribute.intervalInDays.toLong(), "day")}"
        repetitiveAttribute.daysOfWeek != null -> "In ${repetitiveAttribute.daysOfWeek.joinToString(", ") { getShortAbbreviation(it) }}"
        else -> "Unknown interval"
    }

    Row(Modifier.padding(start = 16.dp)) {
        Column {
            Text(
                text = attributeDescription,
                fontSize = 10.scaledSp(),
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
