package net.dzikoysk.presenceofmind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import net.dzikoysk.presenceofmind.components.creator.TaskCreatorDialog
import net.dzikoysk.presenceofmind.components.list.MarkedAs
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
    val selectedTasks = remember { mutableStateOf(MarkedAs.UNFINISHED) }

    Scaffold(
        content = { padding ->
            Column(Modifier.padding(padding)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(top = 22.dp)
                        .fillMaxWidth()
                ) {
                    GlideImage(
                        modifier = Modifier
                            .width(35.dp)
                            .height(35.dp)
                            .clip(CircleShape)
                            .clickable(enabled = true, onClick = { }),
                        imageModel = "https://avatars.githubusercontent.com/u/75123628?s=200&v=4",
                        contentScale = ContentScale.Crop,
                        circularReveal = CircularReveal(duration = 0),
                        placeHolder = ImageVector.vectorResource(R.drawable.ic_baseline_check_circle_24),
                        error = ImageVector.vectorResource(id = R.drawable.ic_baseline_account_circle_24)
                    )

                    TodayLabel(Modifier.padding(horizontal = 16.dp))

                    Row(
                        modifier = Modifier.weight(2f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            painter = painterResource(id = when (selectedTasks.value) {
                                MarkedAs.UNFINISHED -> R.drawable.ic_baseline_grading_24
                                MarkedAs.DONE -> R.drawable.ic_baseline_wrap_text_24
                            }),
                            contentDescription = "Swap tasks",
                            modifier = Modifier.clickable {
                                selectedTasks.value = when (selectedTasks.value) {
                                    MarkedAs.UNFINISHED -> MarkedAs.DONE
                                    MarkedAs.DONE -> MarkedAs.UNFINISHED
                                }
                            }
                        )
                    }
                }

                TaskList(
                    taskService = taskService,
                    displayMode = selectedTasks.value
                )

                TaskCreatorDialog(createTaskDialogState, taskService)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(12.dp),
                backgroundColor = Color.Black,
                onClick = {
                    createTaskDialogState.value = true
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = "Create a new task",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun TodayLabel(modifier: Modifier = Modifier) {
    val todayFormatter = remember {
        DateTimeFormatter.ofPattern("E dd.MM")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    Column(modifier) {
        Row {
            Text(
                text = "Today is ",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777777)
            )
            Text(
                text = todayFormatter.format(Instant.now()),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }
        Row {
            Text(
                text = "Suggested activities for you:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777777)
            )
        }
    }
}
