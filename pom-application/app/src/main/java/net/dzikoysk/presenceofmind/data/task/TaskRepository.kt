package net.dzikoysk.presenceofmind.data.task

import android.content.SharedPreferences
import androidx.core.content.edit
import com.fasterxml.jackson.module.kotlin.readValue
import net.dzikoysk.presenceofmind.shared.DefaultObjectMapper.DEFAULT_OBJECT_MAPPER

sealed interface TaskRepository {

    fun saveOrderedTasks(tasks: List<Task>)

    fun loadOrderedTasks(): List<Task>

}

class SharedPreferencesTaskRepository(
    private val sharedPreferences: SharedPreferences,
    version: String
) : TaskRepository {

    private val orderedTasksId = "ordered-tasks-$version"

    override fun saveOrderedTasks(tasks: List<Task>) {
        val result = DEFAULT_OBJECT_MAPPER.writeValueAsString(tasks)
        sharedPreferences.edit(commit = true) {
            putString(orderedTasksId, result)
        }
        println(result)
    }

    override fun loadOrderedTasks(): List<Task> =
        sharedPreferences.getString(orderedTasksId, null)
            ?.let { DEFAULT_OBJECT_MAPPER.readValue(it) }
            ?: emptyList()

}

class InMemoryTaskRepository : TaskRepository {

    private var storedTasks = emptyList<Task>()

    override fun saveOrderedTasks(tasks: List<Task>) {
        this.storedTasks = tasks.toList()
    }

    override fun loadOrderedTasks(): List<Task> =
        storedTasks.toList()

}