package net.dzikoysk.presenceofmind.model.task.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import net.dzikoysk.presenceofmind.PresenceOfMind
import net.dzikoysk.presenceofmind.PresenceOfMindActivity
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.model.task.attributes.date.getHumanReadableInterval
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import net.dzikoysk.presenceofmind.shared.DefaultTimeProvider
import net.dzikoysk.presenceofmind.shared.TimeProvider
import java.time.Instant
import java.util.UUID

const val CHANNEL_ID = "pom-event-alarm-receiver"
const val EVENT_TASK_EXTRA_ID = "pom-event-task-id"
const val EVENT_TASK_REMINDER_TIME = "pom-event-task-reminder-time"

class AlarmReceiver(private val timeProvider: TimeProvider = DefaultTimeProvider()) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val presenceOfMind = PresenceOfMind.getInstance(context)

        val task = intent.getStringExtra(EVENT_TASK_EXTRA_ID)
            ?.let { UUID.fromString(it) }
            ?.let { presenceOfMind.taskService.findTaskById(it) }
            ?.takeIf { it.eventAttribute != null }
            ?: return

        val eventAttribute = task.eventAttribute
            ?.takeIf { it.reminder?.scheduledAt == Instant.ofEpochMilli(intent.getLongExtra(EVENT_TASK_REMINDER_TIME, 0)) }
            ?.takeUnless { it.eventDate.toLocalDateTime().isBefore(timeProvider.nowAtDefaultZone()) }
            ?: return

        presenceOfMind.taskService.saveTask(task.copy(
            eventAttribute = eventAttribute.copy(
                reminder = eventAttribute.reminder!!.copy(
                    scheduledAt = null
                )
            )
        ))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = notificationManager.findChannel()

        val notificationBuilder = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(task.description)
            .setContentText("In " + eventAttribute.getHumanReadableInterval())
            .setAutoCancel(true)

        val presenceOfMindActivity = Intent(context, PresenceOfMindActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, presenceOfMindActivity, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(task.id.hashCode(), notificationBuilder.build())
    }

    private fun NotificationManager.findChannel() =
        getNotificationChannel(CHANNEL_ID) ?: run {
            val channel = NotificationChannel(CHANNEL_ID, "Presence of Mind Alarms - Events", IMPORTANCE_HIGH)
            channel.description = "Alerts for tasks with event attributes"
            createNotificationChannel(channel)
            channel
        }

}