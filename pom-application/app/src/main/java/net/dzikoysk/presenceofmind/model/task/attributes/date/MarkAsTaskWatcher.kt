package net.dzikoysk.presenceofmind.model.task.attributes.date

import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.model.task.TaskWatcher
import net.dzikoysk.presenceofmind.model.task.isDone
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MarkAsTaskWatcher(private val timeProvider: TimeProvider = DefaultTimeProvider()) : TaskWatcher {

    override fun onRefresh(taskService: TaskService) {
        // marks repetitive tasks as unfinished
        taskService.findAllTasks().asSequence()
            .filter { it.isDone() }
            .filter { it.repetitiveAttribute != null }
            .filter { checkIfTaskIsDoneByRepetitiveAttribute(it, it.repetitiveAttribute!!) }
            .map { it.copy(doneDate = null) }
            .toList()
            .let { taskService.saveTasks(it) }

        // marks repetitive tasks as finished
        taskService.findAllTasks().asSequence()
            .filterNot { it.isDone() }
            .filter { it.repetitiveAttribute != null }
            .filter {
                when {
                    it.repetitiveAttribute?.daysOfWeek != null -> !checkIfTaskIsDoneByRepetitiveAttribute(it, it.repetitiveAttribute)
                    else -> false
                }
            }
            .map { it.copy(doneDate = timeProvider.now().toEpochMilli()) }
            .toList()
            .let { taskService.saveTasks(it) }

        // mark event tasks as finished
        taskService.findAllTasks().asSequence()
            .filterNot { it.isDone() }
            .filter { it.eventAttribute != null && it.eventAttribute.eventDate.toLocalDateTime().isBefore(timeProvider.nowAtDefaultZone()) }
            .map { it.copy(doneDate = timeProvider.now().toEpochMilli()) }
            .toList()
            .let { taskService.saveTasks(it) }
    }

    private fun checkIfTaskIsDoneByRepetitiveAttribute(task: Task, repetitiveAttribute: RepetitiveAttribute): Boolean {
        val doneDate = Instant.ofEpochMilli(task.doneDate ?: 0).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
        val currentDate = timeProvider.now().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()

        return when {
            repetitiveAttribute.intervalInDays != null -> ChronoUnit.DAYS.between(doneDate, currentDate) >= (repetitiveAttribute.intervalInDays)
            repetitiveAttribute.daysOfWeek != null -> doneDate != currentDate && repetitiveAttribute.daysOfWeek.contains(currentDate.dayOfWeek)
            else -> false
        }
    }

}