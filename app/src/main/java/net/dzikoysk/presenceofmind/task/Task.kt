package net.dzikoysk.presenceofmind.task

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

enum class OccurrencePolicy(
    val displayName: String,
    val color: Color
) {
    ONE_TIME("One time", Color(0xffffb5d4)),
    REPETITIVE("Repetitive", Color(0xff7bdfff)),
    LONG_TERM("Long-term", Color(0xffb2eedd))
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "metadata-type"
)
@JsonSubTypes(value = [
    JsonSubTypes.Type(value = OneTimeMetadata::class, name = "one-time"),
    JsonSubTypes.Type(value = RepetitiveMetadata::class, name = "repetitive"),
    JsonSubTypes.Type(value = LongTermMetadata::class, name = "long-term"),
])
sealed class OccurrenceMetadata(
    val policy: OccurrencePolicy
)

/** One-time events */
class OneTimeMetadata(
    val eventDate: EventDateTime = EventDateTime.now()
) : OccurrenceMetadata(OccurrencePolicy.ONE_TIME)


/** Habits / Daily routine **/
class RepetitiveMetadata(
    val intervalInDays: Int = 1,
    val expectedAttentionInMinutes: Int = 60,
    val doneCount: Int = 0,
    val timeSpentInSeconds: Long = 0
) : OccurrenceMetadata(OccurrencePolicy.REPETITIVE)

/** Long-term tasks / notes */
object LongTermMetadata : OccurrenceMetadata(OccurrencePolicy.LONG_TERM)

data class CreateTaskRequest(
    val description: String,
    val policy: OccurrenceMetadata = LongTermMetadata,
    val subtasks: List<SubTask> = emptyList()
)

data class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val done: Long? = null,
    val metadata: OccurrenceMetadata = LongTermMetadata,
    val subtasks: List<SubTask> = emptyList()
)

data class SubTask(
    var index: Int,
    val description: String,
    var done: Boolean
)

data class EventDateTime(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int,
) {

    companion object {
        fun now(): EventDateTime =
            Instant.now().atZone(ZoneId.systemDefault()).run {
                EventDateTime(
                    year = year,
                    month = monthValue,
                    day = dayOfMonth,
                    hour = hour,
                    minute = minute
                )
            }
    }

}

private fun Int.getPrettyNumber(): String =
    String.format("%02d", this)

fun EventDateTime.getDate(): Triple<Int, Int, Int> =
    Triple(year, month, day)

fun EventDateTime.getDateAsString(): String =
    "$year-${(month + 1).getPrettyNumber()}-${day.getPrettyNumber()}"

fun EventDateTime.getTime(): Pair<Int, Int> =
    hour to minute

fun EventDateTime.getTimeAsString(): String =
    "${hour.getPrettyNumber()}:${minute.getPrettyNumber()}"

fun EventDateTime.toLocalDateTime(): Instant =
    LocalDateTime.of(year, month, day, hour, minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()
