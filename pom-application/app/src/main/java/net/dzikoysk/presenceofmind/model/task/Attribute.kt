package net.dzikoysk.presenceofmind.model.task

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore

/** Higher means lower priority */
typealias Priority = Int

/** Default color of task */
val DEFAULT_COLOR = Color(0xFFC3EEFF)

/** Represents task extension */
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
    fun getDefaultAccentColor(): Color =
        DEFAULT_COLOR

    @JsonIgnore
    fun getName(): String

}