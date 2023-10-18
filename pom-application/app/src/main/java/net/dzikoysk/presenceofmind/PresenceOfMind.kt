package net.dzikoysk.presenceofmind

import android.content.Context
import androidx.activity.ComponentActivity
import net.dzikoysk.presenceofmind.model.presence.PresenceRepository
import net.dzikoysk.presenceofmind.model.presence.SharedPreferencesPresenceRepository
import net.dzikoysk.presenceofmind.model.task.SharedPreferencesTaskRepository
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.TaskService
import net.dzikoysk.presenceofmind.model.task.attributes.ChecklistAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.ChecklistEntry
import net.dzikoysk.presenceofmind.model.task.attributes.PomodoroAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.RepetitiveAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventDateTime
import net.dzikoysk.presenceofmind.model.task.attributes.date.MarkAsTaskWatcher
import net.dzikoysk.presenceofmind.model.task.category.CategoryService
import net.dzikoysk.presenceofmind.model.task.category.SharedPreferencesCategoryRepository
import net.dzikoysk.presenceofmind.model.task.reminder.ReminderTaskWatcher

class PresenceOfMind constructor(
    val presenceRepository: PresenceRepository,
    val categoryService: CategoryService,
    val taskService: TaskService,
) {

    companion object {

        private var instance: PresenceOfMind? = null

        fun getInstance(context: Context): PresenceOfMind =
            instance ?: PresenceOfMindFactory.createInstance(context).also { instance = it }

    }
}

private object PresenceOfMindFactory {

    fun createInstance(context: Context): PresenceOfMind {
        val presenceRepository = SharedPreferencesPresenceRepository(
            sharedPreferences = context.getSharedPreferences(
                "net.dzikoysk.presenceofmind.data.theme-repository",
                ComponentActivity.MODE_PRIVATE
            ),
            version = DATA_VERSION
        )

        val categoryService = CategoryService(
            categoryRepository = SharedPreferencesCategoryRepository(
                sharedPreferences = context.getSharedPreferences(
                    "net.dzikoysk.presenceofmind.category-repository",
                    Context.MODE_PRIVATE
                ),
                version = DATA_VERSION
            )
        )

        val taskService = TaskService(
            taskRepository = SharedPreferencesTaskRepository(
                sharedPreferences = context.getSharedPreferences(
                    "net.dzikoysk.presenceofmind.task-repository",
                    Context.MODE_PRIVATE
                ),
                version = DATA_VERSION
            ),
            taskWatchers = listOf(
                MarkAsTaskWatcher(),
                ReminderTaskWatcher(context)
            )
        )

        if (taskService.findAllTasks().isEmpty()) {
            taskService.createDefaultTasks()
        }

        return PresenceOfMind(
            presenceRepository = presenceRepository,
            categoryService = categoryService,
            taskService = taskService
        )
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