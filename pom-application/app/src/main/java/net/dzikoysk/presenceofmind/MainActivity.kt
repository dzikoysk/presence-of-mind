package net.dzikoysk.presenceofmind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import net.dzikoysk.presenceofmind.pages.Router
import net.dzikoysk.presenceofmind.task.SharedPreferencesTaskRepository
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.createDefaultTasks
import net.dzikoysk.presenceofmind.theme.SharedPreferencesThemeRepository
import kotlin.time.ExperimentalTime

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val tasksRepository = TaskService(
            taskRepository = SharedPreferencesTaskRepository(
                this.getSharedPreferences("net.dzikoysk.presenceofmind.tasks-repository.v1.0.0", Context.MODE_PRIVATE)
            )
        )

        tasksRepository.refreshTasksState()

        if (tasksRepository.findAllTasks().isEmpty()) {
            tasksRepository.createDefaultTasks()
        }

        val themeRepository = SharedPreferencesThemeRepository(
            this.getSharedPreferences("net.dzikoysk.presenceofmind.theme-repository.v1.0.0", Context.MODE_PRIVATE)
        )

        super.onCreate(savedInstanceState)

        setTheme(when (themeRepository.isLightMode()) {
            true -> R.style.Theme_LightPresenceOfMind
            false -> R.style.Theme_DarkPresenceOfMind
        })

        setContent {
            Router(
                themeRepository = themeRepository,
                taskService = tasksRepository,
                restartActivity = { recreate() }
            )
        }
    }

}
