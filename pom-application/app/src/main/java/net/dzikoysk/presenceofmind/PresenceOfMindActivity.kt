package net.dzikoysk.presenceofmind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.data.attributes.*
import net.dzikoysk.presenceofmind.data.category.CategoryService
import net.dzikoysk.presenceofmind.data.category.SharedPreferencesCategoryRepository
import net.dzikoysk.presenceofmind.data.task.SharedPreferencesTaskRepository
import net.dzikoysk.presenceofmind.data.task.Task
import net.dzikoysk.presenceofmind.data.task.TaskService
import net.dzikoysk.presenceofmind.data.theme.SharedPreferencesThemeRepository
import net.dzikoysk.presenceofmind.data.theme.ThemeRepository
import net.dzikoysk.presenceofmind.pages.Page
import net.dzikoysk.presenceofmind.pages.Router
import kotlin.time.Duration.Companion.minutes

const val DATA_VERSION = "v1.0.0-RC.5"

class PresenceOfMindActivity : ComponentActivity() {

    private lateinit var themeRepository: ThemeRepository
    private lateinit var categoryService: CategoryService
    private lateinit var taskService: TaskService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.themeRepository = SharedPreferencesThemeRepository(
            sharedPreferences = getSharedPreferences(
                "net.dzikoysk.presenceofmind.data.theme-repository",
                Context.MODE_PRIVATE
            ),
            version = DATA_VERSION
        )

        setTheme(when (themeRepository.isLightMode()) {
            true -> R.style.Theme_LightPresenceOfMind
            false -> R.style.Theme_DarkPresenceOfMind
        })

        this.categoryService = CategoryService(
            categoryRepository = SharedPreferencesCategoryRepository(
                sharedPreferences = getSharedPreferences(
                    "net.dzikoysk.presenceofmind.category-repository",
                    Context.MODE_PRIVATE
                ),
                version = DATA_VERSION
            )
        )

        this.taskService = TaskService(
            taskRepository = SharedPreferencesTaskRepository(
                sharedPreferences = getSharedPreferences(
                    "net.dzikoysk.presenceofmind.task-repository",
                    Context.MODE_PRIVATE
                ),
                version = DATA_VERSION
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
                restartActivity = {
                    recreate()
                },
                page = Page.DASHBOARD
            )
        }
    }

    override fun onStop() {
        taskService.forceTasksSave(sync = true)
        super.onStop()
    }

}

fun TaskService.createDefaultTasks() {
    saveTask(
        Task(description = "**Long-term** task")
    )
    saveTask(
        Task(
            description = "**Event** task",
            eventAttribute = EventAttribute()
        )
    )
    saveTask(
        Task(
            description = "**Habit** task",
            repetitiveAttribute = RepetitiveAttribute(
                intervalInDays = 1,
            )
        )
    )
    saveTask(
        Task(
            description = "**Pomodoro** task",
            pomodoroAttribute = PomodoroAttribute(
                expectedAttentionInMinutes = 75
            )
        )
    )
    saveTask(
        Task(
            description = "**Checklist** task",
            checklistAttribute = ChecklistAttribute(
                list = listOf(ChecklistEntry(description = "Done", done = true))
            )
        )
    )
    saveTask(
        Task(
            description = "**All-in-one** task",
            eventAttribute = EventAttribute(EventDateTime.now().copy(year = 2023)),
            repetitiveAttribute = RepetitiveAttribute(intervalInDays = 1),
            pomodoroAttribute = PomodoroAttribute(90),
            checklistAttribute = ChecklistAttribute(
                list = listOf(
                    ChecklistEntry(description = "https://github.com/dzikoysk/presence-of-mind"),
                    ChecklistEntry(description = "Done", done = true),
                )
            )
        )
    )
}