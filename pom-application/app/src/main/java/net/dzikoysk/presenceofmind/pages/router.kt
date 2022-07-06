package net.dzikoysk.presenceofmind.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.createDefaultTasks
import net.dzikoysk.presenceofmind.theme.InMemoryThemeRepository
import net.dzikoysk.presenceofmind.theme.PresenceOfMindTheme
import net.dzikoysk.presenceofmind.theme.ThemeRepository

/** Redirects to other pages */
@Composable
@Preview(showBackground = true)
fun Router(
    themeRepository: ThemeRepository = InMemoryThemeRepository(),
    taskService: TaskService = TaskService().also { it.createDefaultTasks() },
    restartActivity: () -> Unit = {}
) {
    PresenceOfMindTheme(lightTheme = themeRepository.isLightMode()) {
        Dashboard(
            themeRepository = themeRepository,
            taskService = taskService,
            restartActivity = restartActivity
        )
    }
}