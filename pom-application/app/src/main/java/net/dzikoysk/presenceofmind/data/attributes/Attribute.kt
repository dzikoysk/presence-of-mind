package net.dzikoysk.presenceofmind.data.attributes

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore

/** Higher means lower priority */
typealias Priority = Int

interface Attribute {

    @JsonIgnore
    fun isRunning(): Boolean =
        false

    @JsonIgnore
    fun isConcealable(): Boolean =
        false

    @JsonIgnore
    fun getPriority(): Priority

    @JsonIgnore
    fun getDefaultAccentColor(): Color

    @JsonIgnore
    fun getName(): String

}