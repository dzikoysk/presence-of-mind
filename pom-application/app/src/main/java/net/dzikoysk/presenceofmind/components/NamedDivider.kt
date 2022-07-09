package net.dzikoysk.presenceofmind.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NamedDivider(
    modifier: Modifier = Modifier,
    name: String,
    backgroundColor: Color = MaterialTheme.colors.background,
) {
    val surface = MaterialTheme.colors.secondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
            drawRoundRect(
                color = surface,
                style = Stroke(
                    width = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(Modifier.background(backgroundColor)) {
                Text(
                    text = name,
                    color = surface,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }
        }
    }
}