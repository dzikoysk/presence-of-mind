package net.dzikoysk.presenceofmind.components.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.shared.incomingDurationToHumanReadableFormat
import net.dzikoysk.presenceofmind.shared.plural
import net.dzikoysk.presenceofmind.shared.timeToHumanReadableFormat
import net.dzikoysk.presenceofmind.task.RepetitiveMetadata
import net.dzikoysk.presenceofmind.task.Task
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalMaterialApi
@Preview(showBackground = true)
@ExperimentalTime
@ExperimentalAnimationApi
@Composable
private fun PreviewOfRepetitiveTaskItem() {
    TaskListItem(
        previewMode = true,
        context = TaskItemContext(
            task = Task(
                description = "Preview of repetitive task item",
                metadata = RepetitiveMetadata(
                    intervalInDays = 3,
                    expectedAttentionInMinutes = 30
                )
            )
        )
    )
}

@ExperimentalTime
@Composable
fun createRepetitiveTaskItem(
    previewMode: Boolean = false,
    task: Task,
    metadata: RepetitiveMetadata = task.metadata as RepetitiveMetadata,
) : TaskItemCard {
    val (isOpen, setIsOpen) = remember { mutableStateOf(previewMode) }
    val (isStarted, setIsStarted) = remember { mutableStateOf(previewMode) }
    val (isPaused, setIsPaused) = remember { mutableStateOf(true) }

    val currentIsPaused by rememberUpdatedState(isPaused)
    var seconds by remember { mutableStateOf(0) }

    return TaskItemCard(
        onDone = { updatedTask, markedAs ->
            when (markedAs) {
                MarkedAs.UNFINISHED -> updatedTask
                MarkedAs.DONE -> updatedTask.copy(
                    metadata = metadata.copy(
                        timeSpentInSeconds = metadata.timeSpentInSeconds + seconds
                    )
                )
            }
        },
        content = {
            LaunchedEffect(Unit) {
                while(true) {
                    delay(1.seconds)
                    if (!currentIsPaused) seconds++
                }
            }

            Column {
                Row(Modifier.padding(start = 16.dp)) {
                    Column {
                        Text(
                            text = task.description,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        Text(
                            text = "Every ${plural(metadata.intervalInDays.toLong(), "day")}",
                            fontSize = 10.sp,
                            color = Color.Gray,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 9.dp)
                            .padding(start = 5.dp)
                            .clickable { setIsOpen(!isOpen) }
                    ) {
                        Icon(
                            painter =
                            if (isOpen)
                                painterResource(id = R.drawable.ic_baseline_arrow_drop_up_24)
                            else
                                painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                            contentDescription = "Open repetitive task dashboard"
                        )
                    }
                }

                if (isOpen) {
                    val playbackModifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 8.dp)
                        .size(17.dp)

                    Row(
                        Modifier
                            .padding(start = 8.dp)
                            .padding(top = 8.dp)) {
                        if (isStarted) {
                            Icon(
                                contentDescription = "Stop countdown",
                                painter = painterResource(id = R.drawable.ic_baseline_stop_24),
                                modifier = Modifier
                                    .clickable {
                                        setIsStarted(false)
                                        setIsPaused(true)
                                        seconds = 0
                                    }
                                    .then(playbackModifier)
                            )
                            Icon(
                                contentDescription = "Resume countdown",
                                painter = painterResource(
                                    id =
                                    if (isPaused)
                                        R.drawable.ic_baseline_play_arrow_24
                                    else
                                        R.drawable.ic_baseline_pause_24
                                ),
                                modifier = Modifier
                                    .clickable { setIsPaused(!isPaused) }
                                    .then(playbackModifier)
                            )
                        } else {
                            Icon(
                                contentDescription = "Start countdown",
                                painter = painterResource(id = R.drawable.ic_baseline_play_arrow_24),
                                modifier = Modifier
                                    .clickable {
                                        setIsStarted(true)
                                        setIsPaused(false)
                                    }
                                    .then(playbackModifier)
                            )
                        }

                        Text(
                            text = "${seconds.seconds.timeToHumanReadableFormat()}  /  ${metadata.expectedAttentionInMinutes.minutes.incomingDurationToHumanReadableFormat()}",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 10.dp)
                        )
                    }
                    Row(Modifier.padding(start = 9.dp)) {
                        StatisticsEntry(
                            description = "Replay count",
                            iconId = R.drawable.ic_baseline_sync_24,
                            text = "${task.doneCount} done"
                        )
                        Spacer(
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                        StatisticsEntry(
                            description = "Replay time",
                            iconId = R.drawable.ic_baseline_watch_later_24,
                            text = metadata.timeSpentInSeconds.seconds.timeToHumanReadableFormat()
                        )
                        Spacer(
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )

                        val avg = runCatching { metadata.timeSpentInSeconds / task.doneCount }
                            .getOrDefault(0)
                            .seconds
                            .timeToHumanReadableFormat()

                        StatisticsEntry(
                            description = "Avg replay time",
                            iconId = R.drawable.ic_baseline_stacked_line_chart_24,
                            text = "$avg avg session time"
                        )
                    }
                } else {
                    Box(Modifier.padding(bottom = 10.dp))
                }
            }
        }
    )
}

@Composable
fun StatisticsEntry(
    description: String,
    iconId: Int,
    text: String
) {
    Row(
        Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp, bottom = 10.dp)) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = description,
            tint = Color.Gray,
            modifier = Modifier
                .size(14.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}