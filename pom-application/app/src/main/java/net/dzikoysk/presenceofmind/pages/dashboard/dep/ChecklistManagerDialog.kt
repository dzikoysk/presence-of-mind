package net.dzikoysk.presenceofmind.pages.dashboard.dep//package net.dzikoysk.presenceofmind.pages.dashboard.editor
//
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CornerSize
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.window.DialogProperties
//import net.dzikoysk.presenceofmind.R
//import net.dzikoysk.presenceofmind.shared.scaledSp
//import net.dzikoysk.presenceofmind.task.Task
//import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
//import net.dzikoysk.presenceofmind.task.attributes.ChecklistEntry
//import org.burnoutcrew.reorderable.ReorderableItem
//import org.burnoutcrew.reorderable.detectReorder
//import org.burnoutcrew.reorderable.rememberReorderableLazyListState
//import org.burnoutcrew.reorderable.reorderable
//import java.util.UUID
//
//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun SubtaskManagerDialog(
//    close: () -> Unit,
//    updateTask: (Task) -> Unit,
//    task: Task
//) {
//    Dialog(
//        onDismissRequest = { close() },
//        properties = DialogProperties(usePlatformDefaultWidth = false),
//        content = {
//            SubtaskManager(
//                close = close,
//                updateTask = updateTask,
//                task = task
//            )
//        }
//    )
//}
//
//@Composable
//fun SubtaskManager(
//    close: () -> Unit,
//    updateTask: (Task) -> Unit,
//    task: Task
//) {
//    val subtasks = remember { mutableStateOf(task.subtasks) }
//
//    val subtasksState = rememberReorderableLazyListState(
//        onMove = { from, to ->
//            subtasks.value = subtasks.value.toMutableList().apply {
//                add(to.index, removeAt(from.index))
//            }
//        }
//    )
//
//    Box(
//        modifier = Modifier
//            .padding(12.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .background(MaterialTheme.colors.surface),
//    ) {
//        Column(
//            modifier = Modifier.padding(24.dp),
//        ) {
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SubtaskManagerPreview() {
//    SubtaskManager(
//        close = {},
//        updateTask = {},
//        task = Task(
//            id = UUID.randomUUID(),
//            metadata = EventAttribute(),
//            description = "Preview of one-time task with subtasks",
//            subtasks = listOf(
//                ChecklistEntry(
//                    description = "Subtask 1",
//                    done = true
//                ),
//                ChecklistEntry(
//                    description = "Subtask 2",
//                    done = true
//                ),
//                ChecklistEntry(
//                    description = "Subtask 2",
//                    done = false
//                )
//            )
//        )
//    )
//}