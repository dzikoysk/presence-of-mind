package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.time.Duration.Companion.milliseconds

data class PomodoroAttribute(
    val expectedAttentionInMinutes: Int = 60,
    val timeSpentInSeconds: Long = 0,
    val countdownSession: CountdownSession = CountdownSession()
) : Attribute {

    override fun isRunning(): Boolean =
        countdownSession.isRunning()

    override fun getPriority(): Priority =
        3

    override fun getDefaultAccentColor(): Color =
        Color(0xFFFFA0A0)

    override fun getName(): String =
        "Pomodoro"

}

data class CountdownSession(
    var sessionTimeInSeconds: Long = 0,
    var startTimeInMillis: Long = 0
) {

    fun startCountdown() {
        this.startTimeInMillis = System.currentTimeMillis()
    }

    fun resetCountdown() {
        if (isRunning()) {
            this.sessionTimeInSeconds += (System.currentTimeMillis() - startTimeInMillis).milliseconds.inWholeSeconds
            this.startTimeInMillis = 0
        }
    }

    @JsonIgnore
    fun isRunning(): Boolean =
        startTimeInMillis != 0L

}