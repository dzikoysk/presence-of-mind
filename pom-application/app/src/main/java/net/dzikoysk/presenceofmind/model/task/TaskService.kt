package net.dzikoysk.presenceofmind.model.task

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID


typealias UpdateTask = (Task) -> Unit
typealias SaveTask = (Task) -> Unit
typealias DeleteTask = (Task) -> Unit

class TaskService(
    val taskRepository: TaskRepository = InMemoryTaskRepository(),
    val taskWatchers: Collection<TaskWatcher> = emptyList()
) {

    private val tasks = mutableStateListOf<Task>()

    init {
        tasks.addAll(taskRepository.loadOrderedTasks())
    }

    fun initializeWatchers(extras: Bundle?) =
        taskWatchers.forEach { it.initialize(this, extras) }

    fun refreshTasksState() =
        taskWatchers.forEach { it.onRefresh(this) }

    fun saveTask(task: Task) =
        saveTasks(listOf(task))

    fun saveTasks(collection: Collection<Task>) {
        if (collection.isNotEmpty()) {
            collection.forEach { updateTaskEntry(it) }
            taskRepository.saveOrderedTasks(tasks)
        }
    }

    private fun updateTaskEntry(task: Task) {
        when (val index = tasks.indexOfFirst { it.id == task.id }) {
            -1 -> tasks.add(0, task)
            else -> {
                tasks.removeAt(index)
                tasks.add(index, task)
            }
        }
    }

    fun moveTasks(from: Int, to: Int) {
        tasks.add(to, tasks.removeAt(from))
    }

    fun deleteTask(id: UUID) {
        tasks.removeIf { it.id == id }
        taskRepository.saveOrderedTasks(tasks)
    }

    fun findTaskById(id: UUID): Task? =
        tasks.find { it.id == id }

    fun findAllTasks(): List<Task> =
        tasks.toList()

    fun getObservableListOfAllTasks(): SnapshotStateList<Task> =
        tasks

}