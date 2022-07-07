package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.getAccentColor
import net.dzikoysk.presenceofmind.task.isDone
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(
        context = TaskCardContext(
            task = Task(
                description = "Task with all attributes"
            )
        ),
        state = rememberReorderableLazyListState(onMove = { _, _ -> })
    )
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    state: ReorderableLazyListState,
    context: TaskCardContext,
) {
    val openSubtaskManagerDialog = remember { mutableStateOf(false) }
    val openTaskEditorDialog = remember { mutableStateOf(false) }

    val taskColor =
        context.task.getAccentColor()
            .takeUnless { context.task.isDone() }
            ?: Color.Gray

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .detectReorder(state)
            .then(modifier),
        elevation = 0.dp,
        shape = RoundedCornerShape(CornerSize(8.dp)),
    ) {
        Box(Modifier.drawTaskColorAccent(taskColor)) {
            SwipeableCardContent(
                taskColor = taskColor,
                context = context
            )
        }
    }

    if (openSubtaskManagerDialog.value) {
//        SubtaskManagerDialog(
//            close = { openSubtaskManagerDialog.value = false },
//            updateTask = { context.updateTask(it) },
//            task = context.task
//        )
    }

    if (openTaskEditorDialog.value) {
//        TaskEditor(
//            closeDialog = { openTaskEditorDialog.value = false },
//            saveTask = { context.updateTask(it) },
//            taskToEdit = context.task
//        )
    }
}

private fun Modifier.drawTaskColorAccent(color: Color): Modifier =
    drawWithContent {
        drawContent()
        clipRect {
            drawLine(
                brush = SolidColor(color),
                strokeWidth = 45f,
                cap = StrokeCap.Square,
                start = Offset.Zero,
                end = Offset.Zero.copy(y = size.height)
            )
        }
    }