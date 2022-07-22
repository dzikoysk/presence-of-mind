package net.dzikoysk.presenceofmind.model.task.reminder

import android.content.Context
import android.os.Bundle
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.model.task.TaskWatcher
import net.dzikoysk.presenceofmind.model.task.attributes.date.hasOutdatedSchedule
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import net.dzikoysk.presenceofmind.model.task.isDone
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.temporal.ChronoUnit
import java.util.UUID

class ReminderTaskWatcher(
    private val context: Context,
    private val timeProvider: TimeProvider = DefaultTimeProvider(),
) : TaskWatcher {

    override fun initialize(taskService: TaskService, extras: Bundle?) {
        extras
            ?.getString(EVENT_TASK_EXTRA_ID)
            ?.let { UUID.fromString(it) }
            ?.let { taskService.findTaskById(it) }
            ?.also { task ->
                taskService.saveTask(task.copy(
                    eventAttribute = task.eventAttribute?.copy(
                        reminder = task.eventAttribute.reminder?.copy(
                            scheduledAt = null
                        )
                    )
                ))
            }
    }

    override fun onRefresh(taskService: TaskService) {
        // cleanup old reminders
        taskService.findAllTasks().asSequence()
            .filter { task -> task.eventAttribute?.hasOutdatedSchedule(timeProvider.now()) ?: false }
            .map {
                it.copy(
                    eventAttribute = it.eventAttribute?.copy(
                        reminder = it.eventAttribute.reminder?.copy(
                            scheduledAt = null
                        )
                    )
                )
            }
            .toList()
            .let { taskService.saveTasks(it) }

        // schedule new alarms for event attributes
        taskService.findAllTasks().asSequence()
            .filterNot { it.isDone() }
            .filter { it.eventAttribute?.reminder != null && it.eventAttribute.reminder.scheduledAt == null }
            .map {
                val eventAttribute = it.eventAttribute!!
                val reminder = eventAttribute.reminder!!

                val notificationDateTime = eventAttribute.eventDate
                    .toLocalDateTime()
                    // add extra minute, because we don't have guarantee of execution
                    // at the exact time in millis we've requested from alarm manager
                    .minus(reminder.beforeInMinutes + 1L, ChronoUnit.MINUTES)
                    .takeIf { date -> date.isAfter(timeProvider.nowAtDefaultZone()) }
                    ?: return@map it

                EventReminderReceiver.createNotification(
                    context = context,
                    task = it,
                    ring = reminder.ring,
                    triggerAtMillis = notificationDateTime.toInstant().toEpochMilli()
                )

                it.copy(
                    eventAttribute = eventAttribute.copy(
                        reminder = reminder.copy(
                            scheduledAt = notificationDateTime.toInstant()
                        )
                    )
                )
            }
            .toList()
            .let { taskService.saveTasks(it) }
    }

}