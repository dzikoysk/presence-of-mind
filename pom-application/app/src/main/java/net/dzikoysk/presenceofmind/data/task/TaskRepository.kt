package net.dzikoysk.presenceofmind.data.task

import android.content.SharedPreferences
import androidx.core.content.edit
import com.fasterxml.jackson.module.kotlin.readValue
import net.dzikoysk.presenceofmind.shared.DefaultObjectMapper.DEFAULT_OBJECT_MAPPER
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

sealed interface TaskRepository {

    fun saveOrderedTasks(tasks: List<Task>): Future<*>

    fun loadOrderedTasks(): List<Task>

}

class SharedPreferencesTaskRepository(
    private val sharedPreferences: SharedPreferences,
    version: String
) : TaskRepository {

    private val orderedTasksId = "ordered-tasks-$version"

    private val executor = Executors.newSingleThreadExecutor()

    override fun saveOrderedTasks(tasks: List<Task>): Future<*> =
        executor.submit {
            val result = DEFAULT_OBJECT_MAPPER.writeValueAsString(tasks)
            println(result)
            sharedPreferences.edit { putString(orderedTasksId, result) }
        }

    override fun loadOrderedTasks(): List<Task> =
        sharedPreferences.getString(orderedTasksId, null)
            ?.let { DEFAULT_OBJECT_MAPPER.readValue(it) }
            ?: emptyList()

}

class InMemoryTaskRepository : TaskRepository {

    private var storedTasks = emptyList<Task>()

    override fun saveOrderedTasks(tasks: List<Task>): Future<*> {
        this.storedTasks = tasks.toList()
        return CompletableFuture.completedFuture(null)
    }

    override fun loadOrderedTasks(): List<Task> =
        storedTasks.toList()

}