package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun EditorDrawerPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)) {
        EditorDrawer(
            close = {}
        )
    }
}

@Composable
fun AnimatedEditorDrawer(
    open: Boolean,
    close: () -> Unit
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = open,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        EditorDrawer(
            close = { close() }
        )
    }
}

@Composable
fun EditorDrawer(
    close: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0x44000000))))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { close() }
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x44000000))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(24.dp)
            ) {
                Text(text = "TODO: Create/Edit task")
            }
        }
    }
}