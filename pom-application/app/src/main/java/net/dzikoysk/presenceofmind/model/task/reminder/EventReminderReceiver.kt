package net.dzikoysk.presenceofmind.model.task.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import net.dzikoysk.presenceofmind.PresenceOfMind
import net.dzikoysk.presenceofmind.PresenceOfMindActivity
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.attributes.date.EVENT_DATE_FORMATTER
import net.dzikoysk.presenceofmind.model.task.attributes.date.getHumanReadableInterval
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

const val CHANNEL_ID = "pom-event-reminder-channel"
const val EVENT_TASK_EXTRA_ID = "pom-event-reminder-id"
const val EVENT_TASK_REMINDER_TIME = "pom-event-reminder-time"
const val CANCEL_ACTION = "pom-event-reminder-notification-cancelled"
val DEFAULT_VIBRATE_PATTERN = longArrayOf(0, 250, 250, 250)

class EventReminderReceiver(private val timeProvider: TimeProvider = DefaultTimeProvider()) : BroadcastReceiver() {

    companion object {

        fun createNotification(context: Context, task: Task, ring: Boolean, triggerAtMillis: Long) {
            val intent = Intent(context, EventReminderReceiver::class.java)
            intent.putExtra(EVENT_TASK_EXTRA_ID, task.id.toString())
            intent.putExtra(EVENT_TASK_REMINDER_TIME, triggerAtMillis)
            val pendingIntent = PendingIntent.getBroadcast(context, task.id.hashCode(), intent, FLAG_CANCEL_CURRENT or FLAG_IMMUTABLE)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            alarmManager?.setWindow(AlarmManager.RTC_WAKEUP, triggerAtMillis, 1.seconds.inWholeMilliseconds, pendingIntent)
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        val presenceOfMind = PresenceOfMind.getInstance(context)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val task = intent.getStringExtra(EVENT_TASK_EXTRA_ID)
            ?.let { UUID.fromString(it) }
            ?.let { presenceOfMind.taskService.findTaskById(it) }
            ?.takeIf { it.eventAttribute != null }
            ?: return

        val eventAttribute = task.eventAttribute
            ?.takeIf { it.reminder?.scheduledAt == Instant.ofEpochMilli(intent.getLongExtra(EVENT_TASK_REMINDER_TIME, 0)) }
            ?.takeUnless { it.eventDate.toLocalDateTime().isBefore(timeProvider.nowAtDefaultZone()) }
            ?: return

        val taskNotificationId = task.id.hashCode()

        val nextNotificationTime = Instant.now()
            .takeUnless { intent.action == CANCEL_ACTION }
            ?.plusSeconds(60L)
            .also {
                presenceOfMind.taskService.saveTask(task.copy(
                    eventAttribute = eventAttribute.copy(
                        reminder = eventAttribute.reminder!!.copy(
                            scheduledAt = it
                        )
                    )
                ))
            }
            ?: run {
                notificationManager.cancel(taskNotificationId)
                return
            }

        createNotification(
            context = context,
            task = task,
            ring = false,
            triggerAtMillis = nextNotificationTime.toEpochMilli()
        )

        val deleteIntent = Intent(context, EventReminderReceiver::class.java)
        deleteIntent.action = CANCEL_ACTION
        deleteIntent.putExtra(EVENT_TASK_EXTRA_ID, task.id.toString())
        val pendingDeleteIntent = PendingIntent.getBroadcast(context, taskNotificationId, deleteIntent, FLAG_CANCEL_CURRENT or FLAG_IMMUTABLE)

        val presenceOfMindActivityIntent = Intent(context, PresenceOfMindActivity::class.java)
        presenceOfMindActivityIntent.putExtra(EVENT_TASK_EXTRA_ID, task.id.toString())
        val pendingIntent = PendingIntent.getActivity(context, taskNotificationId, presenceOfMindActivityIntent, FLAG_CANCEL_CURRENT or FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, notificationManager.findChannel().id)
            .setDeleteIntent(pendingDeleteIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(task.description)
            .setContentText("${EVENT_DATE_FORMATTER.format(eventAttribute.eventDate.toLocalDateTime())}, in ${eventAttribute.getHumanReadableInterval()}")
            .setVibrate(DEFAULT_VIBRATE_PATTERN)
            .setColor(Color.RED)
            .setLights(Color.RED, 1000, 1000)
            .setPriority(PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(taskNotificationId, notificationBuilder.build())
    }

    private fun NotificationManager.findChannel() =
        getNotificationChannel(CHANNEL_ID) ?: run {
            val channel = NotificationChannel(CHANNEL_ID, "Presence of Mind - Reminder", IMPORTANCE_HIGH)
            channel.description = "Alerts for tasks with event attributes"
            createNotificationChannel(channel)
            channel
        }

}