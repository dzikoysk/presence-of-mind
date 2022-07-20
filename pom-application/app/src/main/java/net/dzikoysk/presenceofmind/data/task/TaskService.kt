package net.dzikoysk.presenceofmind.data.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import net.dzikoysk.presenceofmind.data.attributes.RepetitiveAttribute
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.concurrent.Executors

typealias UpdateTask = (Task) -> Unit
typealias SaveTask = (Task) -> Unit
typealias DeleteTask = (Task) -> Unit

class TaskService(
    val timeProvider: TimeProvider = DefaultTimeProvider(),
    val taskRepository: TaskRepository = InMemoryTaskRepository()
) {

    private val tasks = mutableStateListOf<Task>()
    private val executor = Executors.newSingleThreadExecutor()

    init {
        tasks.addAll(taskRepository.loadOrderedTasks())
    }

    fun refreshTasksState() {
        // marks tasks as unfinished
        tasks
            .filter { it.isDone() }
            .filter { it.repetitiveAttribute != null }
            .filter { checkIfTaskIsDoneByRepetitiveAttribute(it, it.repetitiveAttribute!!) }
            .forEach { saveTask(it.copy(doneDate = null)) }

        // marks tasks as finished
        tasks
            .filterNot { it.isDone() }
            .filter { it.repetitiveAttribute != null }
            .filter {
                when {
                    it.repetitiveAttribute?.daysOfWeek != null -> !checkIfTaskIsDoneByRepetitiveAttribute(it, it.repetitiveAttribute)
                    else -> false
                }
            }
            .forEach { saveTask(it.copy(doneDate = timeProvider.now().toEpochMilli())) }
    }

    private fun checkIfTaskIsDoneByRepetitiveAttribute(task: Task, repetitiveAttribute: RepetitiveAttribute): Boolean {
        val doneDate = Instant.ofEpochMilli(task.doneDate ?: 0).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
        val currentDate = timeProvider.now().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()

        return when {
            repetitiveAttribute.intervalInDays != null ->
                ChronoUnit.DAYS.between(doneDate, currentDate) >= (repetitiveAttribute.intervalInDays)
            repetitiveAttribute.daysOfWeek != null ->
                doneDate != currentDate && repetitiveAttribute.daysOfWeek.contains(currentDate.dayOfWeek)
            else ->
                false
        }
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