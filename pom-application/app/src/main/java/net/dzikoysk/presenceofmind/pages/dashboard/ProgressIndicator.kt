package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.LinearProgressIndicator

@Composable
fun ProgressIndicator(
    doneTasks: Int,
    allTasks: Int
) {
    val percent = runCatching { doneTasks / allTasks.toFloat() }
        .getOrDefault(1f)

    Row(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(top = 18.dp)
            .fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = percent,
            color = Color(0xFFADA6F8),
            modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
        )
    }
}