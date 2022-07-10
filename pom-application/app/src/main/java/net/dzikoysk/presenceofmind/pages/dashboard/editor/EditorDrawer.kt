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
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.DeleteTask
import net.dzikoysk.presenceofmind.task.SaveTask
import net.dzikoysk.presenceofmind.task.Task

@Composable
fun AnimatedEditorDrawer(
    open: Boolean,
    close: () -> Unit,
    taskToEdit: Task?,
    saveTask: SaveTask,
    deleteTask: DeleteTask
) {
    AnimatedVisibility(
        visible = open,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        EditorDrawer(
            close = { close() },
            taskToEdit = taskToEdit,
            saveTask = saveTask,
            deleteTask = deleteTask
        )
    }
}

enum class EditorTab {
    MAIN,
    CHECKLIST
}

typealias SelectTab = (EditorTab) -> Unit

@Composable
fun EditorDrawer(
    close: () -> Unit,
    taskToEdit: Task?,
    saveTask: SaveTask,
    deleteTask: DeleteTask
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
            ) {
                when (selectedTab.value) {
                    EditorTab.MAIN ->
                        MainMenu(
                            close = close,
                            task = task.value,
                            selectTab = { selectedTab.value = it },
                            updateTask = { task.value = it },
                            saveTask = saveTask,
                            deleteTask = deleteTask
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