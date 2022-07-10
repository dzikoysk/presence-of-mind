package net.dzikoysk.presenceofmind.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.createDefaultTasks
import net.dzikoysk.presenceofmind.pages.dashboard.*
import net.dzikoysk.presenceofmind.pages.dashboard.editor.AnimatedEditorDrawer
import net.dzikoysk.presenceofmind.pages.dashboard.list.TaskList
import net.dzikoysk.presenceofmind.task.MarkedAs
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.theme.InMemoryThemeRepository
import net.dzikoysk.presenceofmind.theme.ThemeRepository

/** List of tasks */

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    Dashboard(
        themeRepository = InMemoryThemeRepository(),
        taskService = TaskService().also { it.createDefaultTasks() },
        restartActivity = {}
    )
}

@Composable
fun Dashboard(
    themeRepository: ThemeRepository,
    taskService: TaskService,
    restartActivity: () -> Unit
) {
    val selectedTasks = remember { mutableStateOf(MarkedAs.UNFINISHED) }
    val openMenu = remember { mutableStateOf(false)  }
    val openEditorDrawer = remember { mutableStateOf<Task?>(null)  }

    Scaffold(
        content = { padding ->
            Box {
                Column(
                    Modifier
                        .padding(padding)
                        .fillMaxHeight()
                        .fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .padding(top = 22.dp)
                            .fillMaxWidth()
                    ) {
                        AvatarImage(
                            openMenu = {  openMenu.value = true  }
                        )
                        TodayLabel(
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Row(
                            modifier = Modifier.weight(2f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            ChangeThemeButton(
                                themeRepository = themeRepository,
                                restartActivity = restartActivity
                            )
                            SwapTasksButton(
                                selectedTasks = selectedTasks.value,
                                selectTask = { selectedTasks.value = it }
                            )
                        }
                    }
                    TaskList(
                        taskService = taskService,
                        openTaskEditor = { openEditorDrawer.value = it },
                        displayMode = selectedTasks.value
                    )
                }
                AnimatedMenuDrawer(
                    open = openMenu.value,
                    close = { openMenu.value = false }
                )

                AnimatedEditorDrawer(
                    open = openEditorDrawer.value != null,
                    close = { openEditorDrawer.value = null },
                    saveTask = { taskService.saveTask(it) },
                    deleteTask = { taskService.deleteTask(it.id) },
                    taskToEdit = openEditorDrawer.value
                )
            }
        },
        floatingActionButton = {
            if (openEditorDrawer.value == null) {
                CreateTaskButton(
                    themeRepository = themeRepository,
                    openTaskEditor = { openEditorDrawer.value = Task() }
                )
            }
        }
    )
}