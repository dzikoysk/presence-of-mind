package net.dzikoysk.presenceofmind.pages.dashboard.list

import net.dzikoysk.presenceofmind.task.Task

data class TaskCardContext(
    val task: Task,
    val openTaskEditor: () -> Unit = {},
    val updateTask: (Task) -> Unit = {},
    val deleteTask: () -> Unit = {},
    val onDone: () -> Unit = {}
)