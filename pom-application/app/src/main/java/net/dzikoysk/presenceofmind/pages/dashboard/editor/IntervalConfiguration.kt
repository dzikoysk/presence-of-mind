package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.UpdateTask
import net.dzikoysk.presenceofmind.task.attributes.IntervalAttribute

@Composable
fun IntervalConfiguration(
    task: Task,
    intervalAttribute: IntervalAttribute,
    updateTask: UpdateTask
) {
    Column(Modifier.padding(12.dp)) {
        OutlinedTextField(
            value = intervalAttribute.intervalInDays.takeIf { it != 0 }?.toString() ?: "",
            label = { Text("Interval in days") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(58.dp),
            onValueChange = {
                updateTask(task.copy(
                    intervalAttribute = intervalAttribute.copy(
                        intervalInDays = it.toIntOrNull() ?: 0
                    ))
                )
            }
        )
    }
}