package net.dzikoysk.presenceofmind.components.creator

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.dzikoysk.presenceofmind.components.NamedDivider
import net.dzikoysk.presenceofmind.task.*

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun TaskCreatorDialog(
    createTaskDialogState: MutableState<Boolean>,
    taskService: TaskService,
) {
    if (!createTaskDialogState.value) {
        return
    }

    Dialog(
        onDismissRequest = {
            createTaskDialogState.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = { TaskCreator(createTaskDialogState, taskService) }
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun TaskCreator(
    createTaskDialogState: MutableState<Boolean> = mutableStateOf(true),
    taskService: TaskService = TaskService(),
) {
    var newTask by remember { mutableStateOf("") }
    val (selectedType, setSelectedType) = remember { mutableStateOf(OccurrencePolicy.ONE_TIME) }
    val (typeDropdownIsOpen, setDropdownIsOpen) = remember { mutableStateOf(false) }
    val (intervalInDays, setIntervalInDays) = remember { mutableStateOf("1") }
    val (expectedAttentionInMinutes, setExpectedAttentionInMinutes) = remember { mutableStateOf("0") }

    val oneTimeEventTime = remember { EventDateTime.now() }
    val (oneTimeDate, setOneTimeDate) = remember { mutableStateOf(oneTimeEventTime.getDateAsString()) }
    val (oneTimeTime, setOneTimeTime) = remember { mutableStateOf(oneTimeEventTime.getTimeAsString()) }

    LazyColumn(
        modifier = Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        contentPadding = PaddingValues(24.dp)
    ) {
        item {
            Text(
                text = "Add a new task  \uD83C\uDFA8",
                modifier = Modifier.padding(vertical = 7.dp),
                fontWeight = FontWeight.Bold
            )
        }

        item {
            OutlinedTextField(
                value = newTask,
                label = { Text("Describe your task") },
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .height(160.dp)
                    .fillMaxWidth(),
                onValueChange = { newTask = it },
            )
        }

        item {
            IntervalOption(
                selectedType = selectedType,
                setSelectedType = setSelectedType,
                typeDropdownIsOpen = typeDropdownIsOpen,
                setDropdownIsOpen = setDropdownIsOpen
            )
        }

        when (selectedType) {
            OccurrencePolicy.ONE_TIME -> {
                item {
                    NamedDivider(
                        name = " ${selectedType.displayName} configuration ",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    val datePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, newYear, newMonth, newDayOfMonth ->
                            oneTimeEventTime.year = newYear
                            oneTimeEventTime.month = newMonth
                            oneTimeEventTime.day = newDayOfMonth
                            setOneTimeDate(oneTimeEventTime.getDateAsString())
                        },
                        oneTimeEventTime.year,
                        oneTimeEventTime.month,
                        oneTimeEventTime.day
                    )

                    OutlinedTextField(
                        value = oneTimeDate,
                        label = { Text(text = "Select the date of the event") },
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() }
                    )

                    val timePickerDialog = TimePickerDialog(
                        LocalContext.current,
                        { picker, hour, minute ->
                            oneTimeEventTime.hour = hour
                            oneTimeEventTime.minute = minute
                            setOneTimeTime(oneTimeEventTime.getTimeAsString())
                        },
                        oneTimeEventTime.hour,
                        oneTimeEventTime.minute,
                        true
                    )

                    OutlinedTextField(
                        value = oneTimeTime,
                        label = { Text(text = "Select the time of the event") },
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clickable { timePickerDialog.show() }
                    )
                }
            }
            OccurrencePolicy.REPETITIVE -> {
                item {
                    NamedDivider(
                        name = " ${selectedType.displayName} configuration ",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = intervalInDays,
                        label = { Text("Interval in days") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { setIntervalInDays(it) }
                    )
                }
                item {
                    OutlinedTextField(
                        value = expectedAttentionInMinutes,
                        label = { Text("Expected attention in minutes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        onValueChange = { setExpectedAttentionInMinutes(it) }
                    )
                }
            }
            OccurrencePolicy.LONG_TERM -> {
                // skip
            }
        }

        item {
            Button(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                onClick = {
                    createTaskDialogState.value = false

                    val policy = when (selectedType) {
                        OccurrencePolicy.ONE_TIME ->
                            OneTimeMetadata(
                                eventDate = oneTimeEventTime
                            )
                        OccurrencePolicy.REPETITIVE ->
                            RepetitiveMetadata(
                                intervalInDays = intervalInDays.toInt(),
                                expectedAttentionInMinutes = expectedAttentionInMinutes.toInt()
                            )
                        OccurrencePolicy.LONG_TERM ->
                            LongTermMetadata
                    }

                    taskService.createTask(
                        CreateTaskRequest(
                            description = newTask,
                            policy = policy
                        )
                    )
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}