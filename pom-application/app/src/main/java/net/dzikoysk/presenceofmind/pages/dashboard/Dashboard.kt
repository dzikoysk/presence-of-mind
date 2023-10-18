package net.dzikoysk.presenceofmind.pages.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.createDefaultTasks
import net.dzikoysk.presenceofmind.model.presence.InMemoryPresenceRepository
import net.dzikoysk.presenceofmind.model.presence.PresenceRepository
import net.dzikoysk.presenceofmind.model.task.MarkedAs
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.pages.Page
import net.dzikoysk.presenceofmind.pages.dashboard.editor.AnimatedEditorDrawer
import net.dzikoysk.presenceofmind.pages.dashboard.editor.TaskToEdit
import net.dzikoysk.presenceofmind.pages.dashboard.list.TaskList

/** List of tasks */

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    Dashboard(
        presenceRepository = InMemoryPresenceRepository(),
        taskService = TaskService().also { it.createDefaultTasks() },
    )
}

@Composable
fun Dashboard(
    presenceRepository: PresenceRepository,
    taskService: TaskService,
    restartActivity: () -> Unit = {},
    changePage: (Page) -> Unit = {}
) {
    val selectedTasks = remember { mutableStateOf(MarkedAs.UNFINISHED) }
    val openMenu = remember { mutableStateOf(false)  }
    val openEditorDrawer = remember { mutableStateOf<TaskToEdit?>(null)  }

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
                            avatarUrl = presenceRepository.getAvatarUrl(),
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
                                presenceRepository = presenceRepository,
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
                        openTaskEditor = { openEditorDrawer.value = TaskToEdit(isNew = false, it) },
                        displayMode = selectedTasks.value
                    )
                }
                AnimatedMenuDrawer(
                    open = openMenu.value,
                    openSettings = { changePage(Page.SETTINGS) },
                    close = { openMenu.value = false }
                )

                AnimatedEditorDrawer(
                    open = openEditorDrawer.value != null,
                    close = { openEditorDrawer.value = null },
                    saveTask = {
                        taskService.saveTask(it)
                        taskService.refreshTasksState()
                    },
                    deleteTask = { taskService.deleteTask(it.id) },
                    taskToEdit = openEditorDrawer.value ?: TaskToEdit(isNew = true)
                )
            }
        },
        floatingActionButton = {
            if (openEditorDrawer.value == null) {
                CreateTaskButton(
                    presenceRepository = presenceRepository,
                    openTaskEditor = { openEditorDrawer.value = TaskToEdit(isNew = true) }
                )
            }
        }
    )
}