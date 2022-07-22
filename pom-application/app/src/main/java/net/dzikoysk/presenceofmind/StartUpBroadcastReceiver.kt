package net.dzikoysk.presenceofmind

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED

internal class StartUpBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_BOOT_COMPLETED) {
            PresenceOfMind.getInstance(context).taskService.refreshTasksState()
        }
    }

}