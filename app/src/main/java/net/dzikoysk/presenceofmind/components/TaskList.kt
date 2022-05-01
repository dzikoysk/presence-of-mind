package net.dzikoysk.presenceofmind.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.store.Task
import org.burnoutcrew.reorderable.*

@Composable
fun TaskList(tasks: SnapshotStateList<Task>) {
    val state = rememberReorderState()

    Box(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .reorderable(state, { from, to -> tasks.move(from.index, to.index) })
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(items = tasks, key = { it.id }) { item ->
                TaskListItem(state, item)
            }
        }
    }
}

@Composable
fun TaskListItem(state: ReorderableState, task: Task) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .draggedItem(state.offsetByKey(task.id))
            .detectReorder(state),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(7.dp))
    ) {
        Row {
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = task.description)
            }
        }
    }
}
