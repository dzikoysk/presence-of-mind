package net.dzikoysk.presenceofmind.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID

class TaskService(
    val taskRepository: TaskRepository = InMemoryTaskRepository()
) {

    private val tasks = mutableStateListOf<Task>()

    init {
        tasks.addAll(taskRepository.loadOrderedTasks())
    }

    fun createTask(createTaskRequest: CreateTaskRequest): Task {
        val task = Task(
            id = UUID.randomUUID(),
            description = createTaskRequest.description,
            metadata = createTaskRequest.policy,
            subtasks = createTaskRequest.subtasks
        )

        tasks.add(task)
        forceTasksSave()

        return task
    }

    fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task
        forceTasksSave()
    }

    fun moveTasks(from: Int, to: Int) {
        tasks.add(to, tasks.removeAt(from))
    }

    fun forceTasksSave() {
        taskRepository.saveOrderedTasks(tasks)
    }

    fun deleteTask(id: UUID) {
        tasks.removeIf { it.id == id }
        forceTasksSave()
    }

    fun findAllTasks(): List<Task> =
        taskRepository.loadOrderedTasks()

    fun getObservableListOfAllTasks(): SnapshotStateList<Task> =
        tasks

}

fun TaskService.createDefaultTasks() {
    createTask(CreateTaskRequest(description = "One-time task ~ Events", policy = OneTimeMetadata()))
    createTask(CreateTaskRequest(
        description = "Repetitive task ~ Habits",
        policy = RepetitiveMetadata(
            intervalInDays = 1,
            expectedAttentionInMinutes = 75
        )
    ))
    createTask(CreateTaskRequest(description = "Long-term task ~ Notes", policy = LongTermMetadata))
    createTask(CreateTaskRequest(description = "Complex long-term task ~ Lists", policy = LongTermMetadata, subtasks = listOf(
        SubTask(index = 0, description = "To do", done = false),
        SubTask(index = 1, description = "Done", done = true),
    )))
}