package net.dzikoysk.presenceofmind.model.task.attributes.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.model.task.Watcher
import net.dzikoysk.presenceofmind.model.task.isDone

class ReminderWatcher(val context: Context) : Watcher {

    override fun refresh(taskService: TaskService) {
        taskService.findAllTasks().asSequence()
            .filterNot { it.isDone() }
            .filter { it.reminderAttribute != null }
            .forEach {
                when {
                    it.eventAttribute != null -> {

                    }
                    it.repetitiveAttribute != null -> {}
                }
            }
    }

    private fun createNotification(context: Context, task: Task, triggerAtMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(TASK_EXTRA_ID, task.id.toString())
        val pendingIntent = PendingIntent.getBroadcast(context, task.id.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

}