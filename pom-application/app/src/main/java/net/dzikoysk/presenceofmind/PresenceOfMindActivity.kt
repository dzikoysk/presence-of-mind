package net.dzikoysk.presenceofmind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.task.SharedPreferencesTaskRepository
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.createDefaultTasks
import net.dzikoysk.presenceofmind.theme.SharedPreferencesThemeRepository
import kotlin.time.Duration.Companion.minutes

class PresenceOfMindActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val themeRepository = SharedPreferencesThemeRepository(
            this.getSharedPreferences("net.dzikoysk.presenceofmind.theme-repository.v1.0.0", Context.MODE_PRIVATE)
        )

        super.onCreate(savedInstanceState)

        setTheme(when (themeRepository.isLightMode()) {
            true -> R.style.Theme_LightPresenceOfMind
            false -> R.style.Theme_DarkPresenceOfMind
        })

        val taskService = TaskService(
            taskRepository = SharedPreferencesTaskRepository(
                this.getSharedPreferences("net.dzikoysk.presenceofmind.tasks-repository.v1.0.0", Context.MODE_PRIVATE)
            )
        )

        taskService.refreshTasksState()

        if (taskService.findAllTasks().isEmpty()) {
            taskService.createDefaultTasks()
        }

        setContent {
            LaunchedEffect(Unit) {
                while(true) {
                    delay(1.minutes)
                    taskService.refreshTasksState()
                }
            }

            Router(
                themeRepository = themeRepository,
                taskService = taskService,
                restartActivity = { recreate() }
            )
        }
    }

}
