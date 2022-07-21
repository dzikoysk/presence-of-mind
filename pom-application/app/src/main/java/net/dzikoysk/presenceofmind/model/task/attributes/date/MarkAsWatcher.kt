package net.dzikoysk.presenceofmind.model.task.attributes.date

import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.model.task.Watcher
import net.dzikoysk.presenceofmind.model.task.attributes.RepetitiveAttribute
import net.dzikoysk.presenceofmind.model.task.isDone
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MarkAsWatcher(private val timeProvider: TimeProvider = DefaultTimeProvider()) : Watcher {

    override fun onRefresh(taskService: TaskService) {
        // marks tasks as unfinished
        taskService.findAllTasks().asSequence()
            .filter { it.isDone() }
            .filter { it.repetitiveAttribute != null }
            .filter { checkIfTaskIsDoneByRepetitiveAttribute(it, it.repetitiveAttribute!!) }
            .forEach { taskService.saveTask(it.copy(doneDate = null)) }

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
            .forEach { taskService.saveTask(it.copy(doneDate = timeProvider.now().toEpochMilli())) }

        // mark event tasks as finished
        taskService.findAllTasks().asSequence()
            .filterNot { it.isDone() }
            .filter { it.eventAttribute != null && it.eventAttribute.eventDate.toLocalDateTime().isBefore(timeProvider.nowAtDefaultZone()) }
            .forEach { taskService.saveTask(it.copy(doneDate = timeProvider.now().toEpochMilli())) }
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