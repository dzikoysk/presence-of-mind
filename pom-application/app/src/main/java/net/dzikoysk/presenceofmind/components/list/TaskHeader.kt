package net.dzikoysk.presenceofmind.components.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.androidpoet.dropdown.*

@ExperimentalAnimationApi
@Composable
fun TaskHeader(
    content: @Composable (() -> Unit),
    deleteTask: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            content()
            TaskMenu(deleteTask = deleteTask)
        }
    }
}

const val DELETE_TASK_ID = "delete-task"

@ExperimentalAnimationApi
@Composable
fun TaskMenu(deleteTask: () -> Unit) {
    Box {
        val (isOpen, setIsOpen) = remember { mutableStateOf(false) }

        TaskMenuDropdown(isOpen = isOpen, setIsOpen = setIsOpen, itemSelected = {
            when (it) {
                DELETE_TASK_ID -> deleteTask()
            }
            setIsOpen(false)
        })

        IconButton(
            onClick = { setIsOpen(true) }
        ) {
            Icon(
                imageVector = Icons.TwoTone.MoreVert,
                contentDescription = "Open task dropdown menu"
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TaskMenuDropdown(
    isOpen: Boolean = false,
    setIsOpen: (Boolean) -> Unit,
    itemSelected: (String) -> Unit
) {
    val menu = dropDownMenu<String> {
        item(id = "add-subtask", title = "Add subtask")
        item(id = DELETE_TASK_ID, title = "Delete")
    }

    Dropdown(
        isOpen = isOpen,
        menu = menu,
        colors = dropDownMenuColors(
            backgroundColor = Color.LightGray,
            contentColor = Color.Black
        ),
        onItemSelected = itemSelected,
        onDismiss = { setIsOpen(false) },
        offset = DpOffset(8.dp, 0.dp),
        enter = EnterAnimation.ElevationScale,
        exit = ExitAnimation.ElevationScale,
        easing = Easing.FastOutSlowInEasing,
        enterDuration = 400,
        exitDuration = 400
    )
}