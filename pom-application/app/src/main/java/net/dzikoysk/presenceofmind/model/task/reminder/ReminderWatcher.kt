package net.dzikoysk.presenceofmind.model.task.reminder

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.model.task.Watcher
import net.dzikoysk.presenceofmind.model.task.attributes.date.hasOutdatedSchedule
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import net.dzikoysk.presenceofmind.model.task.isDone
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.temporal.ChronoUnit

class ReminderWatcher(
    private val context: Context,
    private val timeProvider: TimeProvider = DefaultTimeProvider(),
) : Watcher {

    override fun onRefresh(taskService: TaskService) {
        // cleanup old reminders
        taskService.findAllTasks().asSequence()
            .filter { task -> task.eventAttribute?.hasOutdatedSchedule(timeProvider.now()) ?: false }
            .map {
                it.copy(
                    eventAttribute = it.eventAttribute!!.copy(
                        reminder = it.eventAttribute.reminder!!.copy(
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

                createNotification(
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

    private fun createNotification(task: Task, ring: Boolean, triggerAtMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EVENT_TASK_EXTRA_ID, task.id.toString())
        intent.putExtra(EVENT_TASK_REMINDER_TIME, triggerAtMillis)
        val pendingIntent = PendingIntent.getBroadcast(context, task.id.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.setExactAndAllowWhileIdle(RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

}