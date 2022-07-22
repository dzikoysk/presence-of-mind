package net.dzikoysk.presenceofmind.model.task

import android.os.Bundle

interface TaskWatcher {

    fun initialize(taskService: TaskService, extras: Bundle?) {}

    fun onRefresh(taskService: TaskService)

}