package net.dzikoysk.presenceofmind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.creator.TaskCreatorDialog
import net.dzikoysk.presenceofmind.components.list.TaskList
import net.dzikoysk.presenceofmind.task.SharedPreferencesTaskRepository
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.createDefaultTasks
import net.dzikoysk.presenceofmind.theme.PresenceOfMindTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.ExperimentalTime

@ExperimentalMaterialApi
@ExperimentalTime
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val tasksRepository = TaskService(
            taskRepository = SharedPreferencesTaskRepository(
                this.getSharedPreferences("net.dzikoysk.presenceofmind.tasks-repository", Context.MODE_PRIVATE)
            )
        )

        if (tasksRepository.findAllTasks().isEmpty()) {
            tasksRepository.createDefaultTasks()
        }

        super.onCreate(savedInstanceState)
        setContent {
            PresenceOfMindTheme {
                MainView(
                    taskService = tasksRepository
                )
            }
        }
    }

}

@ExperimentalMaterialApi
@ExperimentalTime
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
@Preview(showBackground = true)
fun MainView(
    taskService: TaskService = TaskService().also { it.createDefaultTasks() }
) {
    val createTaskDialogState = remember { mutableStateOf(false)  }

    Scaffold(
        content = { padding ->
            Column(Modifier.padding(padding)) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(top = 22.dp)
                        .fillMaxWidth()
                ) {
                    val todayFormatter = DateTimeFormatter.ofPattern("E dd.MM")
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault())

                    Text(
                        text = "Today is ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = todayFormatter.format(Instant.now()),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(
                        text = ", suggested activities:",
                        fontWeight = FontWeight.Bold
                    )
                }

                TaskList(taskService)
                TaskCreatorDialog(createTaskDialogState, taskService)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(12.dp),
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
