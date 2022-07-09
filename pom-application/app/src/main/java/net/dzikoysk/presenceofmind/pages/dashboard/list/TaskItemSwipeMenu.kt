package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R

@Composable
fun TaskItemSwipeMenu(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isDone: Boolean,
    onDone: () -> Unit,
) {
    val iconId =
        if (isDone)
            R.drawable.ic_baseline_replay_24
        else
            R.drawable.ic_baseline_check_24

    Box(modifier) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "Done",
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .fillMaxSize()
                .clickable {
                    if (isVisible) {
                        onDone()
                    }
                },
        )
    }
}