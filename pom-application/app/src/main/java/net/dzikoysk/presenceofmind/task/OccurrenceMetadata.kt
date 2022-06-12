package net.dzikoysk.presenceofmind.task

import androidx.compose.ui.graphics.Color

enum class OccurrenceType(
    val displayName: String,
    val color: Color
) {
    ONE_TIME("One time", Color(0xFFFFCCF8)),
    REPETITIVE("Repetitive", Color(0xFFBBF0B1)),
    LONG_TERM("Long-term", Color(0xFFC3EEFF))
}

open class OccurrenceMetadata(
    val policy: OccurrenceType,
)