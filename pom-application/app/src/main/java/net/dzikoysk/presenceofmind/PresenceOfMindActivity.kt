package net.dzikoysk.presenceofmind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.task.SharedPreferencesTaskRepository
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.attributes.*
import net.dzikoysk.presenceofmind.theme.SharedPreferencesThemeRepository
import kotlin.time.Duration.Companion.minutes

const val DATA_VERSION = "v1.0.0-RC.5-1"

class PresenceOfMindActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val themeRepository = SharedPreferencesThemeRepository(
            this.getSharedPreferences("net.dzikoysk.presenceofmind.theme-repository.$DATA_VERSION", Context.MODE_PRIVATE)
        )

        super.onCreate(savedInstanceState)

        setTheme(when (themeRepository.isLightMode()) {
            true -> R.style.Theme_LightPresenceOfMind
            false -> R.style.Theme_DarkPresenceOfMind
        })

        val taskService = TaskService(
            taskRepository = SharedPreferencesTaskRepository(
                this.getSharedPreferences("net.dzikoysk.presenceofmind.tasks-repository.$DATA_VERSION", Context.MODE_PRIVATE)
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
                restartActivity = { recreate() },
                page = Page.DASHBOARD
            )
        }
    }

}

fun TaskService.createDefaultTasks() {
    saveTask(
        Task(
        description = "One-time task ~ Events",
        eventAttribute = EventAttribute()
    )
    )
    saveTask(
        Task(
        description = "Repetitive task ~ Habits",
        intervalAttribute = IntervalAttribute(
            intervalInDays = 1,
        )
    )
    )
    saveTask(
        Task(
        description = "Pomodoro task",
        pomodoroAttribute = PomodoroAttribute(
            expectedAttentionInMinutes = 75
        )
    )
    )
    saveTask(
        Task(description = "Long-term task ~ Notes")
    )
    saveTask(
        Task(
        description = "Complex long-term task ~ Lists",
        checklistAttribute = ChecklistAttribute(
            list = listOf(
                ChecklistEntry(description = "To do", done = false),
                ChecklistEntry(description = "Done", done = true),
            )
        )
    )
    )
}