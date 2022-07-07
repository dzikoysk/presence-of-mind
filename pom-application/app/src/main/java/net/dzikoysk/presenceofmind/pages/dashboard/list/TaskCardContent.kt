package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.SubTaskList
import net.dzikoysk.presenceofmind.task.Task

@Composable
fun TaskCardContent(
    task: Task,
    updateTask: (Task) -> Unit,
    deleteTask: () -> Unit,
    content: @Composable (() -> Unit)
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(
                top = 10.dp,
                bottom = if (task.subtasksAttribute != null) 10.dp else 0.dp
            )
    ) {
        Column {
            content()

            SubTaskList(
                task = task,
                updateTask = { updateTask(it) }
            )
        }

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.End,
//        ) {
//            TaskMenu(
//                deleteTask = deleteTask,
//                editTask = editTask,
//                openSubtasksManager = openSubtasksManager
//            )
//        }
    }

}

//const val MANAGE_SUBTASKS_ID = "manage-subtasks"
//const val EDIT_TASK_ID = "edit-task"
//const val DELETE_TASK_ID = "delete-task"
//
//@ExperimentalAnimationApi
//@Composable
//fun TaskMenu(
//    deleteTask: () -> Unit,
//    editTask: () -> Unit,
//    openSubtasksManager: () -> Unit
//) {
//    Box {
//        val (isOpen, setIsOpen) = remember { mutableStateOf(false) }
//
//        TaskMenuDropdown(
//            isOpen = isOpen,
//            setIsOpen = setIsOpen,
//            itemSelected = {
//                when (it) {
//                    DELETE_TASK_ID -> deleteTask()
//                    EDIT_TASK_ID -> editTask()
//                    MANAGE_SUBTASKS_ID -> openSubtasksManager()
//                }
//                setIsOpen(false)
//            }
//        )
//         Icon(
//            imageVector = Icons.TwoTone.MoreVert,
//            contentDescription = "Open task dropdown menu",
//            modifier = Modifier
//                .size(24.dp)
//                .clickable { setIsOpen(true) }
//        )
//    }
//}
//
//@ExperimentalAnimationApi
//@Composable
//fun TaskMenuDropdown(
//    isOpen: Boolean = false,
//    setIsOpen: (Boolean) -> Unit,
//    itemSelected: (String) -> Unit
//) {
//    val menu = dropDownMenu<String> {
//        item(id = MANAGE_SUBTASKS_ID, title = "\uD83D\uDCDC    Manage subtasks")
//        item(id = EDIT_TASK_ID, title = "\u2712\uFE0F    Edit task")
//        item(id = DELETE_TASK_ID, title = "\uD83D\uDDD1    Delete")
//    }
//
//    Dropdown(
//        isOpen = isOpen,
//        menu = menu,
//        colors = dropDownMenuColors(
//            backgroundColor = MaterialTheme.colors.background,
//            contentColor = MaterialTheme.colors.onBackground
//        ),
//        onItemSelected = itemSelected,
//        onDismiss = { setIsOpen(false) },
//        offset = DpOffset(8.dp, 0.dp),
//        enter = EnterAnimation.ElevationScale,
//        exit = ExitAnimation.ElevationScale,
//        easing = Easing.FastOutSlowInEasing,
//        enterDuration = 400,
//        exitDuration = 400
//    )
//}