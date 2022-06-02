package net.dzikoysk.presenceofmind.components.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.LongTermMetadata
import net.dzikoysk.presenceofmind.task.Task

@Composable
fun LongTermTaskItem(task: Task, metadata: LongTermMetadata) {
    Column(Modifier.padding(start = 16.dp)) {
        Text(
            text = task.description,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}