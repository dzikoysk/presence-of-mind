package net.dzikoysk.presenceofmind.pages.dashboard

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.model.presence.PresenceRepository

@Composable
fun CreateTaskButton(
    presenceRepository: PresenceRepository,
    openTaskEditor: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        backgroundColor =
            when (presenceRepository.isLightMode()) {
                true -> MaterialTheme.colors.primaryVariant
                false -> MaterialTheme.colors.surface
            },
        onClick = { openTaskEditor() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Create a new task",
            tint = Color.White
        )
    }
}