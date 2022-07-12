package net.dzikoysk.presenceofmind.pages.dashboard.list

import net.dzikoysk.presenceofmind.data.task.Task
import net.dzikoysk.presenceofmind.data.task.UpdateTask

data class TaskCardContext(
    val task: Task,
    val openTaskEditor: () -> Unit = {},
    val updateTask: UpdateTask = {},
    val deleteTask: () -> Unit = {},
)