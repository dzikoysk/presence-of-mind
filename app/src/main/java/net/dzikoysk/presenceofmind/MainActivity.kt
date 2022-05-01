package net.dzikoysk.presenceofmind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.ui.theme.PresenceOfMindTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.burnoutcrew.reorderable.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PresenceOfMindTheme {
                MyApp()
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun MyApp() {
    val formatter = DateTimeFormatter.ofPattern("dd.MM").withZone(ZoneId.systemDefault())
    val tasks = listOf(Task( title = "Title a"), Task( title = "Title b")).toMutableStateList()
    val createTaskDialogState = remember { mutableStateOf(false)  }

    Scaffold(
        content = {
            Text(
                text = "Today is ${formatter.format(Instant.now())}, suggested activities:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            TaskList(tasks)
            CreateTaskDialog(createTaskDialogState, tasks)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createTaskDialogState.value = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun CreateTaskDialog(createTaskDialogState: MutableState<Boolean>, tasks: SnapshotStateList<Task>) {
    if (!createTaskDialogState.value) {
        return
    }

    Dialog(
        onDismissRequest = {
            createTaskDialogState.value = false
        },
        properties = DialogProperties(),
        content = {
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
                            tasks.add(Task(title = newTask))
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    )
}

@Composable
fun TaskList(tasks: SnapshotStateList<Task>) {
    val state = rememberReorderState()

    Box(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .reorderable(state, { from, to -> tasks.move(from.index, to.index) })
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(items = tasks, key = { it.id }) { item ->
                TaskListItem(state, item)
            }
        }
    }
}

@Composable
fun TaskListItem(state: ReorderableState, task: Task) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .draggedItem(state.offsetByKey(task.id))
            .detectReorder(state),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(7.dp))
    ) {
        Row {
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = task.title)
            }
        }
    }
}

