package net.dzikoysk.presenceofmind.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.theme.PresenceOfMindTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.TaskCreatorDialog
import net.dzikoysk.presenceofmind.components.TaskList
import net.dzikoysk.presenceofmind.store.TaskRepository
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PresenceOfMindTheme {
                PresenceOfMind()
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PresenceOfMind() {
    val formatter = DateTimeFormatter.ofPattern("dd.MM").withZone(ZoneId.systemDefault())
    val tasksRepository = TaskRepository()
    val tasks = tasksRepository.findAllTasks().toMutableStateList()
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
            TaskCreatorDialog(createTaskDialogState, tasks)
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
