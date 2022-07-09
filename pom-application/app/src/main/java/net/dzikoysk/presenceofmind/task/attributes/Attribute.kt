package net.dzikoysk.presenceofmind.task.attributes

import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore

interface Attribute {

    @JsonIgnore
    fun isRunning(): Boolean =
        false

    @JsonIgnore
    fun getDefaultAccentColor(): Color? =
        null

    @JsonIgnore
    fun getName(): String

}