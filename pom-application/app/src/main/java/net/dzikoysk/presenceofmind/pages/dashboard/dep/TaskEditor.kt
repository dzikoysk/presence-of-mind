//package net.dzikoysk.presenceofmind.pages.dashboard.editor
//
//import android.app.DatePickerDialog
//import android.app.TimePickerDialog
//import android.widget.DatePicker
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import net.dzikoysk.presenceofmind.components.NamedDivider
//import net.dzikoysk.presenceofmind.components.editor.TaskEditorIntervalOption
//import net.dzikoysk.presenceofmind.shared.scaledSp
//import net.dzikoysk.presenceofmind.task.Task
//import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
//import net.dzikoysk.presenceofmind.task.attributes.IntervalAttribute
//import net.dzikoysk.presenceofmind.task.attributes.getDateAsString
//import net.dzikoysk.presenceofmind.task.attributes.getTimeAsString
//
//@Preview(showBackground = true)
//@Composable
//fun TaskEditorPreview() {
//    TaskEditor(
//        closeDialog = {},
//        saveTask = {},
//        taskToEdit = null
//    )
//}
//
//@Composable
//fun TaskEditor(
//    closeDialog: () -> Unit,
//    saveTask: (Task) -> Unit,
//
//) {
//
//
//    Box(
//        modifier = Modifier
//            .padding(24.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .background(MaterialTheme.colors.surface)
//    ) {
//        Column(
//            modifier = Modifier.padding(24.dp),
//        ) {
//
//
//
//
//            TaskEditorIntervalOption(
//                selectedType = task.metadata.type,
//                setSelectedType = { task = task.copy(metadata = it.createDefaultInstance()) },
//            )
//
//            when (task.metadata.type) {
//                OccurrenceType.ONE_TIME -> {
//                    val metadata = task.metadata as EventAttribute
//                    val eventDate = metadata.eventDate
//
//                    NamedDivider(
//                        name = " ${metadata.type.displayName} configuration ",
//                        backgroundColor = MaterialTheme.colors.surface,
//                        modifier = Modifier.padding(vertical = 10.dp)
//                    )
//
//                    val datePickerDialog = DatePickerDialog(
//                        LocalContext.current,
//                        { _: DatePicker, newYear, newMonth, newDayOfMonth ->
//                            task = task.copy(
//                                metadata = metadata.copy(
//                                    eventDate = eventDate.copy(
//                                        year = newYear,
//                                        month = newMonth,
//                                        day = newDayOfMonth
//                                    )
//                                )
//                            )
//                        },
//                        eventDate.year,
//                        eventDate.month,
//                        eventDate.day
//                    )
//
//                    OutlinedTextField(
//                        value = eventDate.getDateAsString(),
//                        label = { Text(text = "Select the date of the event") },
//                        onValueChange = {},
//                        enabled = false,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(58.dp)
//                            .clickable { datePickerDialog.show() }
//                    )
//
//                    val timePickerDialog = TimePickerDialog(
//                        LocalContext.current,
//                        { _, hour, minute ->
//                            task = task.copy(
//                                metadata = metadata.copy(
//                                    eventDate = eventDate.copy(
//                                        hour = hour,
//                                        minute = minute
//                                    )
//                                )
//                            )
//                        },
//                        eventDate.hour,
//                        eventDate.minute,
//                        true
//                    )
//
//                    OutlinedTextField(
//                        value = eventDate.getTimeAsString(),
//                        label = { Text(text = "Select the time of the event") },
//                        onValueChange = {},
//                        enabled = false,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 10.dp)
//                            .height(58.dp)
//                            .clickable { timePickerDialog.show() }
//                    )
//                }
//                OccurrenceType.REPETITIVE -> {
//                    val metadata = task.metadata as IntervalAttribute
//
//                    // val (intervalInDays, setIntervalInDays) = remember { mutableStateOf("1") }
//                    // val (expectedAttentionInMinutes, setExpectedAttentionInMinutes) = remember { mutableStateOf("0") }
//
//                    NamedDivider(
//                        name = " ${metadata.type.displayName} configuration ",
//                        backgroundColor = MaterialTheme.colors.surface,
//                        modifier = Modifier.padding(vertical = 10.dp)
//                    )
//                    OutlinedTextField(
//                        value = metadata.intervalInDays.toString(),
//                        label = { Text("Interval in days") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(58.dp),
//                        onValueChange = {
//                            task = task.copy(
//                                metadata = metadata.copy(
//                                    intervalInDays = it.toIntOrNull() ?: 0
//                                )
//                            )
//                        }
//                    )
//                    OutlinedTextField(
//                        value = metadata.expectedAttentionInMinutes.toString(),
//                        label = { Text("Expected attention in minutes") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 10.dp)
//                            .height(58.dp),
//                        onValueChange = {
//                            task = task.copy(
//                                metadata = metadata.copy(
//                                    expectedAttentionInMinutes = it.toIntOrNull() ?: 0
//                                )
//                            )
//                        }
//                    )
//                }
//                OccurrenceType.LONG_TERM -> {
//                    task = task.copy(metadata = LongTermMetadata)
//                }
//            }
//
//
//        }
//    }
//}
