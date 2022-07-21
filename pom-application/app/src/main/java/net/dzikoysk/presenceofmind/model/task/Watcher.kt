package net.dzikoysk.presenceofmind.model.task

interface Watcher {

    fun onRefresh(taskService: TaskService)

}