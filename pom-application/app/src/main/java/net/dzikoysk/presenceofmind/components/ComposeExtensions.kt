package net.dzikoysk.presenceofmind.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val LocalFontScale: ProvidableCompositionLocal<Float> = compositionLocalOf { 1f }

@Composable
fun Int.scaledSp(): TextUnit =
    (this@scaledSp / LocalDensity.current.fontScale * LocalFontScale.current).sp
