package net.dzikoysk.presenceofmind.store

class TaskRepository {

    fun findAllTasks(): List<Task> {
        return listOf(Task( description = "Title a"), Task( description = "Title b"))
    }

}