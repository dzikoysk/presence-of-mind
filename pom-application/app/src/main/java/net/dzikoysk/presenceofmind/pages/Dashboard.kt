package net.dzikoysk.presenceofmind.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.pages.dashboard.*
import net.dzikoysk.presenceofmind.pages.dashboard.editor.AnimatedEditorDrawer
import net.dzikoysk.presenceofmind.pages.dashboard.list.TaskList
import net.dzikoysk.presenceofmind.task.MarkedAs
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.createDefaultTasks
import net.dzikoysk.presenceofmind.theme.InMemoryThemeRepository
import net.dzikoysk.presenceofmind.theme.ThemeRepository

/** List of tasks */

@Composable
fun Dashboard(
    themeRepository: ThemeRepository = InMemoryThemeRepository(),
    taskService: TaskService = TaskService().also { it.createDefaultTasks() },
    restartActivity: () -> Unit = {}
) {
    val selectedTasks = remember { mutableStateOf(MarkedAs.UNFINISHED) }
    val openMenu = remember { mutableStateOf(false)  }
    val openEditorDrawer = remember { mutableStateOf(false)  }

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
                        displayMode = selectedTasks.value
                    )
                }
                AnimatedMenuDrawer(
                    open = openMenu.value,
                    close = { openMenu.value = false }
                )

                AnimatedEditorDrawer(
                    open = openEditorDrawer.value,
                    close = { openEditorDrawer.value = false },
                    // saveTask = { taskService.saveTask(it) },
                    // taskToEdit = null
                )
            }
        },
        floatingActionButton = {
            if (!openEditorDrawer.value) {
                CreateTaskButton(
                    themeRepository = themeRepository,
                    openTaskEditor = { openEditorDrawer.value = true }
                )
            }
        }
    )
}