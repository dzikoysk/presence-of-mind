package net.dzikoysk.presenceofmind.task.attributes

import androidx.annotation.CheckResult
import androidx.compose.ui.graphics.Color
import kotlin.time.Duration.Companion.milliseconds

data class PomodoroAttribute(
    val expectedAttentionInMinutes: Int = 60,
    val timeSpentInSeconds: Long = 0,
    val countdownSession: CountdownSession = CountdownSession()
) : Attribute {

    override fun isRunning(): Boolean =
        countdownSession.isRunning()

    override fun isConcealable(): Boolean =
        true

    override fun getPriority(): Priority =
        3

    override fun getDefaultAccentColor(): Color =
        Color(0xFFFFA0A0)

    override fun getName(): String =
        "Pomodoro"

}

data class CountdownSession(
    val sessionTimeInSeconds: Long = 0,
    val startTimeInMillis: Long = 0
)

fun CountdownSession.isRunning(): Boolean =
    startTimeInMillis != 0L

@CheckResult
fun CountdownSession.withStartedCountdown(): CountdownSession =
    copy(startTimeInMillis = System.currentTimeMillis())

@CheckResult
fun CountdownSession.withAccumulatedCountdown(): CountdownSession =
    when {
        isRunning() -> copy(
            sessionTimeInSeconds = sessionTimeInSeconds + (System.currentTimeMillis() - startTimeInMillis).milliseconds.inWholeSeconds,
            startTimeInMillis = 0
        )
        else -> this
    }