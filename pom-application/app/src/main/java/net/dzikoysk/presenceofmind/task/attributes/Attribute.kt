package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore

/** Higher means lower priority */
typealias Priority = Int

interface Attribute {

    @JsonIgnore
    fun isRunning(): Boolean =
        false

    @JsonIgnore
    fun getPriority(): Priority

    @JsonIgnore
    fun getDefaultAccentColor(): Color

    @JsonIgnore
    fun getName(): String

}