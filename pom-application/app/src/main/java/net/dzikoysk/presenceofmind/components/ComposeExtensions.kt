package net.dzikoysk.presenceofmind.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Int.scaledSp(): TextUnit =
    with(LocalDensity.current) {
        (this@scaledSp / this.fontScale).sp
    }
