package net.dzikoysk.presenceofmind

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.dzikoysk.presenceofmind.pages.Dashboard
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.task.createDefaultTasks
import net.dzikoysk.presenceofmind.theme.InMemoryThemeRepository
import net.dzikoysk.presenceofmind.theme.PresenceOfMindTheme
import net.dzikoysk.presenceofmind.theme.ThemeRepository

enum class Page {
    DASHBOARD
}

/** Redirects to other pages */
@Composable
@Preview(showBackground = true)
fun Router(
    themeRepository: ThemeRepository = InMemoryThemeRepository(),
    taskService: TaskService = TaskService().also { it.createDefaultTasks() },
    restartActivity: () -> Unit = {},
    page: Page = Page.DASHBOARD
) {
    PresenceOfMindTheme(lightTheme = themeRepository.isLightMode()) {
        when (page) {
            Page.DASHBOARD ->
                Dashboard(
                    themeRepository = themeRepository,
                    taskService = taskService,
                    restartActivity = restartActivity
                )
        }
    }
}