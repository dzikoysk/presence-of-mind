package net.dzikoysk.presenceofmind.pages.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.task.MarkedAs

@Composable
fun SwapTasksButton(
    selectedTasks: MarkedAs,
    selectTask: (MarkedAs) -> Unit
) {
    Icon(
        contentDescription = "Swap tasks",
        painter = painterResource(
            id = when (selectedTasks) {
                MarkedAs.UNFINISHED -> R.drawable.ic_baseline_grading_24
                MarkedAs.DONE -> R.drawable.ic_baseline_wrap_text_24
            }
        ),
        modifier = Modifier
            .clickable {
                selectTask(
                    when (selectedTasks) {
                        MarkedAs.UNFINISHED -> MarkedAs.DONE
                        MarkedAs.DONE -> MarkedAs.UNFINISHED
                    }
                )
            }
            .size(25.dp)
            .padding(top = 1.dp)
    )
}