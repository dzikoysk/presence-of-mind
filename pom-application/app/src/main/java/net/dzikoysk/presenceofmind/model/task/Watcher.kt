package net.dzikoysk.presenceofmind.model.task

interface Watcher {

    fun refresh(taskService: TaskService)

}