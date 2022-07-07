package net.dzikoysk.presenceofmind.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import net.dzikoysk.presenceofmind.task.attributes.*
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID

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
            .filter { it.interval != null }
            .filter {
                val doneDate = Instant.ofEpochMilli(it.doneDate ?: 0).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
                val currentDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
                return@filter ChronoUnit.DAYS.between(doneDate, currentDate) >= it.interval!!.intervalInDays
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

fun TaskService.createDefaultTasks() {
    saveTask(Task(
        description = "One-time task ~ Events",
        eventAttribute = EventAttribute()
    ))
    saveTask(Task(
        description = "Repetitive task ~ Habits",
        interval = IntervalAttribute(
            intervalInDays = 1,
        )
    ))
    saveTask(Task(
        description = "Pomodoro task",
        pomodoro = PomodoroAttribute(
            expectedAttentionInMinutes = 75
        )
    ))
    saveTask(
        Task(description = "Long-term task ~ Notes")
    )
    saveTask(Task(
        description = "Complex long-term task ~ Lists",
        checklistAttribute = ChecklistAttribute(
            list = listOf(
                ChecklistEntry(description = "To do", done = false),
                ChecklistEntry(description = "Done", done = true),
            )
        )
    ))
}