package net.dzikoysk.presenceofmind.pages.dashboard.list

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.SwipeState
import net.dzikoysk.presenceofmind.components.SwipeableCard
import net.dzikoysk.presenceofmind.components.drawTaskColorAccent
import net.dzikoysk.presenceofmind.model.task.MarkedAs
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.getAccentColor
import net.dzikoysk.presenceofmind.model.task.isDone
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

typealias OnDone = (Task, MarkedAs) -> Task
typealias SubscribeToOnDone = (OnDone) -> Unit

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    state: ReorderableLazyListState,
    context: TaskCardContext,
) {
    val task = context.task
    val taskColor =
        task.getAccentColor()
            .takeUnless { task.isDone() }
            ?: Color.Gray

    val subscribedToOnDone = remember { mutableListOf<OnDone>() }

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
                swipeThreshold = 0.7f,
                menuBackgroundColor = taskColor,
                onStateChange = { swipeContext ->
                    when (swipeContext.currentState()) {
                        SwipeState.LEFT -> {
                            val markedAsDone = if (task.isDone()) MarkedAs.UNFINISHED else MarkedAs.DONE

                            var updatedTask = task.copy(
                                doneDate = if (task.isDone()) null else System.currentTimeMillis(),
                                doneCount = task.doneCount + markedAsDone.ordinal
                            )

                            subscribedToOnDone.forEach { onDone ->
                                updatedTask = onDone(updatedTask, markedAsDone)
                            }

                            context.updateTask(updatedTask)
                            println(updatedTask)
                            swipeContext.snapTo(SwipeState.CONTENT)

                            SweetSuccess(
                                message = when (markedAsDone) {
                                    MarkedAs.DONE -> "Marked as done"
                                    MarkedAs.UNFINISHED -> "Task has been restored!"
                                },
                                duration = Toast.LENGTH_SHORT,
                                padding = PaddingValues(top = 16.dp),
                                contentAlignment = Alignment.TopCenter
                            )
                        }
                        SwipeState.CONTENT -> {}
                        SwipeState.RIGHT -> {
                            context.openTaskEditor()
                            swipeContext.snapTo(SwipeState.CONTENT)
                        }
                    }
                },
                leftContent = { _, _ ->
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        Icon(
                            painter = painterResource(id = when {
                                task.isDone() -> R.drawable.ic_baseline_replay_24
                                else -> R.drawable.ic_baseline_check_24
                            }),
                            contentDescription = "Done",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                content = { _, _ ->
                    TaskCardContent(
                        task = task,
                        updateTask = context.updateTask,
                        subscribeToOnDone = { subscribedToOnDone.add(it) }
                    )
                },
                rightContent = { _, _ ->
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                            contentDescription = "Done",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            )
        }
    }
}