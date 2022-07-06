package net.dzikoysk.presenceofmind.task.attributes

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.time.Duration.Companion.milliseconds

data class PomodoroAttribute(
    val expectedAttentionInMinutes: Int = 60,
    val timeSpentInSeconds: Long = 0,
    val countdownSession: CountdownSession = CountdownSession()
)

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