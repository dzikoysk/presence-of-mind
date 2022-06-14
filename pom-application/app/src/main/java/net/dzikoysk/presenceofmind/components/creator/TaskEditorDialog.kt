package net.dzikoysk.presenceofmind.components.creator

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.dzikoysk.presenceofmind.components.NamedDivider
import net.dzikoysk.presenceofmind.task.*


@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun TaskEditorPreview() {
    TaskEditor(
        closeDialog = {},
        saveTask = {},
        taskToEdit = null
    )
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun TaskEditorDialog(
    closeDialog: () -> Unit,
    saveTask: (Task) -> Unit,
    taskToEdit: Task?
) {
    Dialog(
        onDismissRequest = { closeDialog() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            TaskEditor(
                closeDialog = closeDialog,
                saveTask = saveTask,
                taskToEdit = taskToEdit
            )
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun TaskEditor(
    closeDialog: () -> Unit,
    saveTask: (Task) -> Unit,
    taskToEdit: Task?
) {
    var task by remember { mutableStateOf(taskToEdit ?: Task(description = "", metadata = OneTimeMetadata())) }

    Box(
        modifier = Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "Add a new task  \uD83C\uDFA8",
                modifier = Modifier.padding(vertical = 7.dp),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = task.description,
                label = { Text("Describe your task") },
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .height(160.dp)
                    .fillMaxWidth(),
                onValueChange = { task = task.copy(description = it) },
            )

            TaskEditorIntervalOption(
                selectedType = task.metadata.type,
                setSelectedType = { task = task.copy(metadata = it.createDefaultInstance()) },
            )

            when (task.metadata.type) {
                OccurrenceType.ONE_TIME -> {
                    val metadata = task.metadata as OneTimeMetadata
                    val eventDate = metadata.eventDate

                    NamedDivider(
                        name = " ${metadata.type.displayName} configuration ",
                        backgroundColor = MaterialTheme.colors.surface,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    val datePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, newYear, newMonth, newDayOfMonth ->
                            task = task.copy(
                                metadata = metadata.copy(
                                    eventDate = eventDate.copy(
                                        year = newYear,
                                        month = newMonth,
                                        day = newDayOfMonth
                                    )
                                )
                            )
                        },
                        eventDate.year,
                        eventDate.month,
                        eventDate.day
                    )

                    OutlinedTextField(
                        value = eventDate.getDateAsString(),
                        label = { Text(text = "Select the date of the event") },
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() }
                    )

                    val timePickerDialog = TimePickerDialog(
                        LocalContext.current,
                        { _, hour, minute ->
                            task = task.copy(
                                metadata = metadata.copy(
                                    eventDate = eventDate.copy(
                                        hour = hour,
                                        minute = minute
                                    )
                                )
                            )
                        },
                        eventDate.hour,
                        eventDate.minute,
                        true
                    )

                    OutlinedTextField(
                        value = eventDate.getTimeAsString(),
                        label = { Text(text = "Select the time of the event") },
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clickable { timePickerDialog.show() }
                    )
                }
                OccurrenceType.REPETITIVE -> {
                    val metadata = task.metadata as RepetitiveMetadata

                    // val (intervalInDays, setIntervalInDays) = remember { mutableStateOf("1") }
                    // val (expectedAttentionInMinutes, setExpectedAttentionInMinutes) = remember { mutableStateOf("0") }

                    NamedDivider(
                        name = " ${metadata.type.displayName} configuration ",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    OutlinedTextField(
                        value = metadata.intervalInDays.toString(),
                        label = { Text("Interval in days") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            task = task.copy(
                                metadata = metadata.copy(
                                    intervalInDays = it.toIntOrNull() ?: 0
                                )
                            )
                        }
                    )
                    OutlinedTextField(
                        value = metadata.expectedAttentionInMinutes.toString(),
                        label = { Text("Expected attention in minutes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        onValueChange = {
                            task = task.copy(
                                metadata = metadata.copy(
                                    expectedAttentionInMinutes = it.toIntOrNull() ?: 0
                                )
                            )
                        }
                    )
                }
                OccurrenceType.LONG_TERM -> {
                    task = task.copy(metadata = LongTermMetadata)
                }
            }

            Button(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                onClick = {
                    saveTask(task)
                    closeDialog()
                },
                content = { Text(text = "Save") }
            )
        }
    }
}
