package net.dzikoysk.presenceofmind.pages.dashboard.list.attributes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.model.task.attributes.*
import net.dzikoysk.presenceofmind.model.task.MarkedAs
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.UpdateTask
import net.dzikoysk.presenceofmind.pages.dashboard.list.SubscribeToOnDone
import net.dzikoysk.presenceofmind.shared.incomingDurationToHumanReadableFormat
import net.dzikoysk.presenceofmind.shared.timeToHumanReadableFormat
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Preview(showBackground = true)
@Composable
fun PomodoroAttributeRendererPreview() {
    PomodoroAttributeRenderer(
        task = Task(
            description = "Pomodoro task",
            pomodoroAttribute = PomodoroAttribute(
                expectedAttentionInMinutes = 30
            )
        ),
        subscribeToOnDone = { },
        updateTask = {}
    )
}

@Composable
fun PomodoroAttributeRenderer(
    task: Task,
    subscribeToOnDone: SubscribeToOnDone,
    updateTask: UpdateTask
) {
    val pomodoroAttribute = task.pomodoroAttribute!!
    val countdownSession = pomodoroAttribute.countdownSession

    val (isStarted, setIsStarted) = remember { mutableStateOf(countdownSession.isRunning()) }
    var countdownWatcher by remember { mutableStateOf(0) }

    subscribeToOnDone { updatedTask, markedAs ->
        when (markedAs) {
            MarkedAs.UNFINISHED -> updatedTask
            MarkedAs.DONE ->
                updatedTask.copy(
                    pomodoroAttribute = pomodoroAttribute.copy(
                        timeSpentInSeconds = pomodoroAttribute.timeSpentInSeconds + countdownSession.withAccumulatedCountdown().sessionTimeInSeconds,
                        countdownSession = CountdownSession()
                    )
                )
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            countdownWatcher++
            delay(500.milliseconds)
        }
    }

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
                painter = painterResource(id = R.drawable.ic_baseline_pause_24),
                modifier = Modifier
                    .clickable {
                        setIsStarted(false)
                        updateTask(task.copy(
                            pomodoroAttribute = pomodoroAttribute.copy(
                                countdownSession = countdownSession.withAccumulatedCountdown()
                            )
                        ))
                    }
                    .then(playbackModifier)
            )
        } else {
            Icon(
                contentDescription = "Start countdown",
                painter = painterResource(id = R.drawable.ic_baseline_play_arrow_24),
                modifier = Modifier
                    .clickable {
                        setIsStarted(true)
                        updateTask(task.copy(
                            pomodoroAttribute = pomodoroAttribute.copy(
                                countdownSession = countdownSession.withStartedCountdown()
                            )
                        ))
                    }
                    .then(playbackModifier)
            )
        }

        val timeToFinish = pomodoroAttribute.expectedAttentionInMinutes
            .minutes
            .incomingDurationToHumanReadableFormat()

        val currentTimeInSeconds = countdownWatcher.run { countdownSession.startTimeInMillis }
            .takeIf { countdownSession.isRunning() }
            ?.let { System.currentTimeMillis() - it }
            ?.milliseconds
            ?.inWholeSeconds
            ?: 0

        val timeElapsed = currentTimeInSeconds + countdownSession.sessionTimeInSeconds

        Text(
            text = "${timeElapsed.seconds.timeToHumanReadableFormat()}  /  $timeToFinish",
            fontSize = 10.scaledSp(),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 11.dp)
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
            text = pomodoroAttribute.timeSpentInSeconds.seconds.timeToHumanReadableFormat()
        )
        Spacer(
            modifier = Modifier.padding(horizontal = 5.dp)
        )

        val avg = runCatching { pomodoroAttribute.timeSpentInSeconds / task.doneCount }
            .getOrDefault(0)
            .seconds
            .timeToHumanReadableFormat()

        StatisticsEntry(
            description = "Avg replay time",
            iconId = R.drawable.ic_baseline_stacked_line_chart_24,
            text = "$avg average session"
        )
    }
}

@Composable
fun StatisticsEntry(
    description: String,
    iconId: Int,
    text: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = description,
            tint = Color.Gray,
            modifier = Modifier
                .size(14.dp)
                .padding(top = 0.dp)
        )
        Text(
            text = text,
            fontSize = 10.scaledSp(),
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}