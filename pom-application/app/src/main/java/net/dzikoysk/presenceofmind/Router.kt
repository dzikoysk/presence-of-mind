package net.dzikoysk.presenceofmind

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.dzikoysk.presenceofmind.pages.Dashboard
import net.dzikoysk.presenceofmind.task.TaskService
import net.dzikoysk.presenceofmind.theme.InMemoryThemeRepository
import net.dzikoysk.presenceofmind.theme.PresenceOfMindTheme
import net.dzikoysk.presenceofmind.theme.ThemeRepository

/** Redirects to other pages */

enum class Page {
    DASHBOARD
}

@Composable
@Preview(showBackground = true)
fun RouterPreview() {
    Router(
        themeRepository = InMemoryThemeRepository(),
        taskService = TaskService().also { it.createDefaultTasks() },
        restartActivity = {},
        page = Page.DASHBOARD
    )
}

@Composable
fun Router(
    themeRepository: ThemeRepository,
    taskService: TaskService,
    restartActivity: () -> Unit,
    page: Page
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