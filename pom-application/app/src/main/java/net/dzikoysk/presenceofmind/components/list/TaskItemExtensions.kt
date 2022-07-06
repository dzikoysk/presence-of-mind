package net.dzikoysk.presenceofmind.components.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import net.dzikoysk.presenceofmind.shared.scaledSp
import net.dzikoysk.presenceofmind.task.Task
import java.lang.Integer.max

@Composable
fun Task.scaledFontSize(): TextUnit =
    max(11, 15 - (description.length / 20)).scaledSp()