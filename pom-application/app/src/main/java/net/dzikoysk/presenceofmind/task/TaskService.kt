package net.dzikoysk.presenceofmind.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID

typealias UpdateTask = (Task) -> Unit
typealias SaveTask = (Task) -> Unit
typealias DeleteTask = (Task) -> Unit

class TaskService(
    val taskRepository: TaskRepository = InMemoryTaskRepository()
) {

    private val tasks = mutableStateListOf<Task>()

    init {
        tasks.addAll(taskRepository.loadOrderedTasks())
    }

    fun refreshTasksState() {
        tasks
            .filter { it.isDone() }
            .filter { it.intervalAttribute != null }
            .filter {
                val doneDate = Instant.ofEpochMilli(it.doneDate ?: 0).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
                val currentDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
                return@filter ChronoUnit.DAYS.between(doneDate, currentDate) >= it.intervalAttribute!!.intervalInDays
            }
            .forEach { saveTask(it.copy(doneDate = null)) }
    }

    fun saveTask(task: Task) {
        when (val index = tasks.indexOfFirst { it.id == task.id }) {
            -1 -> tasks.add(0, task)
            else -> {
                tasks.removeAt(index)
                tasks.add(index, task)
            }
        }
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