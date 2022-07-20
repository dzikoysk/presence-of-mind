package net.dzikoysk.presenceofmind.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.dzikoysk.presenceofmind.createDefaultTasks
import net.dzikoysk.presenceofmind.model.presence.InMemoryPresenceRepository
import net.dzikoysk.presenceofmind.model.presence.PresenceOfMindTheme
import net.dzikoysk.presenceofmind.model.presence.PresenceRepository
import net.dzikoysk.presenceofmind.model.task.TaskService

/** Redirects to other pages */

enum class Page {
    DASHBOARD
}

@Composable
@Preview(showBackground = true)
fun RouterPreview() {
    Router(
        presenceRepository = InMemoryPresenceRepository(),
        taskService = TaskService().also { it.createDefaultTasks() },
        restartActivity = {},
        page = Page.DASHBOARD
    )
}

@Composable
fun Router(
    presenceRepository: PresenceRepository,
    taskService: TaskService,
    restartActivity: () -> Unit,
    page: Page
) {
    PresenceOfMindTheme(lightTheme = presenceRepository.isLightMode()) {
        when (page) {
            Page.DASHBOARD ->
                Dashboard(
                    presenceRepository = presenceRepository,
                    taskService = taskService,
                    restartActivity = restartActivity
                )
        }
    }
}