package net.dzikoysk.presenceofmind.model.task.attributes.reminder

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
import java.util.UUID

const val CHANNEL_ID = "pom-alarm-receiver"
const val TASK_EXTRA_ID = "pom-task-id"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val presenceOfMind = PresenceOfMind.getInstance(context)

        val task = intent.getStringExtra(TASK_EXTRA_ID)
            ?.let { UUID.fromString(it) }
            ?.let { presenceOfMind.taskService.findTaskById(it) }
            ?: return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = notificationManager.getNotificationChannel(CHANNEL_ID) ?: run {
            val channel = NotificationChannel(CHANNEL_ID, "Presence of Mind Alarms", IMPORTANCE_HIGH)
            channel.description = "Presence Of Mind Alarms & Notifications"
            notificationManager.createNotificationChannel(channel)
            channel
        }

        val notificationBuilder = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(task.description)
            .setContentText("Task reminder")
            .setAutoCancel(true)

        val presenceOfMindActivity = Intent(context, PresenceOfMindActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, presenceOfMindActivity, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(task.id.hashCode(), notificationBuilder.build())
    }

}