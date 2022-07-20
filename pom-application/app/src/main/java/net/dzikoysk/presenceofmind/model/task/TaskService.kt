package net.dzikoysk.presenceofmind.model.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID
import java.util.concurrent.Executors


typealias UpdateTask = (Task) -> Unit
typealias SaveTask = (Task) -> Unit
typealias DeleteTask = (Task) -> Unit

class TaskService(
    val taskRepository: TaskRepository = InMemoryTaskRepository(),
    val watchers: Collection<Watcher> = emptyList()
) {

    private val tasks = mutableStateListOf<Task>()
    private val executor = Executors.newSingleThreadExecutor()

    init {
        tasks.addAll(taskRepository.loadOrderedTasks())
    }

    fun refreshTasksState() =
        watchers.forEach {
            it.refresh(this)
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

    fun forceTasksSave(sync: Boolean = false) {
        when (sync) {
            true -> taskRepository.saveOrderedTasks(tasks)
            false -> executor.submit { forceTasksSave(sync = true) }
        }
    }

    fun deleteTask(id: UUID) {
        tasks.removeIf { it.id == id }
        forceTasksSave()
    }

    fun findTaskById(id: UUID): Task? =
        tasks.find { it.id == id }

    fun findAllTasks(): List<Task> =
        tasks.toList()

    fun getObservableListOfAllTasks(): SnapshotStateList<Task> =
        tasks

}