package net.dzikoysk.presenceofmind.task

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

enum class OccurrenceType(
    val displayName: String,
    val color: Color,
    val createDefaultInstance: () -> OccurrenceMetadata
) {
    ONE_TIME("One time", Color(0xFFFFCCF8), { OneTimeMetadata() }),
    REPETITIVE("Repetitive", Color(0xFFBBF0B1), { RepetitiveMetadata() }),
    LONG_TERM("Long-term", Color(0xFFC3EEFF), { LongTermMetadata })
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
open class OccurrenceMetadata(
    @JsonAlias(value = [ "policy" ])
    val type: OccurrenceType,
)