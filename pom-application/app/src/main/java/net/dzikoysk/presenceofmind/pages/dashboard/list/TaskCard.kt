package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.shared.SwipeState
import net.dzikoysk.presenceofmind.shared.SwipeableCard
import net.dzikoysk.presenceofmind.shared.drawTaskColorAccent
import net.dzikoysk.presenceofmind.task.MarkedAs
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
            SwipeableCard(
                menuSize = 55.dp,
                menuBackgroundColor = taskColor,
                leftContent = { swipeState, snapTo ->
                    TaskItemSwipeMenu(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        isVisible = swipeState == SwipeState.LEFT,
                        isDone = context.task.isDone(),
                        onDone = {
                            val markedAsDone = if (context.task.isDone()) MarkedAs.UNFINISHED else MarkedAs.DONE

                            context.task
                                .copy(
                                    doneDate = if (context.task.isDone()) null else System.currentTimeMillis(),
                                    doneCount = context.task.doneCount + markedAsDone.ordinal
                                )
                                //.let { taskItemCard.onDone(it, markedAsDone) } TODO: notify task supervisor
                                .apply { context.updateTask(this) }

                            snapTo(SwipeState.CONTENT)
                        }
                    )
                },
                content = { swipeState, snapTo ->
                    TaskCardContent(
                        task = context.task,
                        updateTask = context.updateTask,
                        deleteTask = context.deleteTask,
                        // content = { taskItemCard.content() }, TODO: specific task impl
                    )
                },
                rightEntry = { swipeState, snapTo ->
                    Box(Modifier.padding(horizontal = 18.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                            contentDescription = "Done",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .fillMaxSize()
                                .clickable {
                                    if (swipeState == SwipeState.RIGHT) {
                                        context.openTaskEditor()
                                        snapTo(SwipeState.CONTENT)
                                    }
                                },
                        )
                    }
                }
            )
        }
    }
}