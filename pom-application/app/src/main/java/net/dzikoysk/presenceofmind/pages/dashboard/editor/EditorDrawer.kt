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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.attributes.ChecklistAttribute

@Preview(showBackground = true)
@Composable
fun EditorDrawerPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)) {
        EditorDrawer(
            close = {},
            taskToEdit = Task(
                description = "Task to edit",
                checklistAttribute = ChecklistAttribute()
            ),
            saveTask = {}
        )
    }
}

@Composable
fun AnimatedEditorDrawer(
    open: Boolean,
    close: () -> Unit,
    taskToEdit: Task?,
    saveTask: (Task) -> Unit
) {
    AnimatedVisibility(
        visible = open,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        EditorDrawer(
            close = { close() },
            taskToEdit = taskToEdit,
            saveTask = saveTask
        )
    }
}

enum class EditorTab {
    MAIN,
    CHECKLIST
}

@Composable
fun EditorDrawer(
    close: () -> Unit,
    taskToEdit: Task?,
    saveTask: (Task) -> Unit
) {
    val task = remember { mutableStateOf(taskToEdit ?: Task(description = "")) }
    val interactionSource = remember { MutableInteractionSource() }
    val selectedTab = remember { mutableStateOf(EditorTab.MAIN) }

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
                    .padding(horizontal = 24.dp)
            ) {
                when (selectedTab.value) {
                    EditorTab.MAIN ->
                        MainMenu(
                            close = close,
                            task = task.value,
                            selectTab = { selectedTab.value = it },
                            updateTask = { task.value = it },
                            saveTask = saveTask,
                        )
                    EditorTab.CHECKLIST ->
                        ChecklistEditor(
                            task = task.value,
                            updateTask = { task.value = it },
                            close = { selectedTab.value = EditorTab.MAIN }
                        )
                }
            }
        }
    }
}