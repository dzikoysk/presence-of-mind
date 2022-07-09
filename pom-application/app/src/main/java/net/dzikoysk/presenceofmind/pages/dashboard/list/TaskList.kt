package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.components.NamedDivider
import net.dzikoysk.presenceofmind.createDefaultTasks
import net.dzikoysk.presenceofmind.task.MarkedAs
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.isDone

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    TaskList(
        taskService = TaskService().also { it.createDefaultTasks() },
        openTaskEditor = {},
        displayMode = MarkedAs.UNFINISHED
    )
}

@Composable
fun TaskList(
    taskService: TaskService,
    openTaskEditor: (Task) -> Unit,
    displayMode: MarkedAs
) {
    val tasks = taskService.getObservableListOfAllTasks()
    val doneTasks = tasks.filter { it.isDone() }
    val matchedTasks = when (displayMode) {
        MarkedAs.UNFINISHED -> tasks.filterNot { it.isDone() }
        MarkedAs.DONE -> doneTasks
    }

    ProgressIndicator(
        doneTasks = doneTasks.size,
        allTasks = tasks.size
    )
    ReorderableListOfTasks(
        taskService = taskService,
        openTaskEditor = openTaskEditor,
        indexOfTask = { tasks.indexOfFirst { task -> task.id == it } },
        tasks = matchedTasks
    )

    if (matchedTasks.isEmpty()) {
        NamedDivider(
            name = "No matched tasks in this group",
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp)
        )
    }
}
