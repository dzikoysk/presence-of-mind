package net.dzikoysk.presenceofmind.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.dzikoysk.presenceofmind.store.Task

@Composable
fun TaskCreatorDialog(createTaskDialogState: MutableState<Boolean>, tasks: SnapshotStateList<Task>) {
    if (!createTaskDialogState.value) {
        return
    }

    Dialog(
        onDismissRequest = {
            createTaskDialogState.value = false
        },
        properties = DialogProperties(),
        content = { TaskCreator(createTaskDialogState, tasks) }
    )
}

@Composable
@Preview(showBackground = true)
fun TaskCreator(
    createTaskDialogState: MutableState<Boolean> = mutableStateOf(true),
    tasks: SnapshotStateList<Task> = emptyList<Task>().toMutableStateList()
) {
    Box(Modifier.background(Color.White)) {
        Column(modifier = Modifier.padding(24.dp)) {
            var newTask by remember { mutableStateOf("") }

            Text(
                text = "Add a new task",
                modifier = Modifier.padding(vertical = 7.dp)
            )
            TextField(
                value = newTask,
                modifier = Modifier.padding(vertical = 7.dp),
                placeholder = { Text("Task") },
                onValueChange = { newTask = it },
            )
            Button(
                modifier = Modifier.padding(vertical = 7.dp),
                onClick = {
                    createTaskDialogState.value = false
                    tasks.add(Task(description = newTask))
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}