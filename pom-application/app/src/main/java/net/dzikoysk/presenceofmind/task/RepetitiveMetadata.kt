package net.dzikoysk.presenceofmind.task

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.time.Duration.Companion.milliseconds

/** Habits / Pomodoro / Daily routine **/
data class RepetitiveMetadata(
    val intervalInDays: Int = 1,
    val expectedAttentionInMinutes: Int = 60,
    val timeSpentInSeconds: Long = 0,
    val countdownSession: CountdownSession = CountdownSession()
) : OccurrenceMetadata(OccurrenceType.REPETITIVE)

data class CountdownSession(
    var sessionTimeInSeconds: Long = 0,
    var startTimeInMillis: Long = 0
) {

    fun startCountdown() {
        this.startTimeInMillis = System.currentTimeMillis()
    }

    fun resetCountdown() {
        this.sessionTimeInSeconds = sessionTimeInSeconds + (System.currentTimeMillis() - startTimeInMillis).milliseconds.inWholeSeconds
        this.startTimeInMillis = 0
    }

    @JsonIgnore
    fun isRunning(): Boolean =
        startTimeInMillis != 0L

}

