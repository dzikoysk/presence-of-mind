package net.dzikoysk.presenceofmind.pages.dashboard.editor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.UpdateTask
import net.dzikoysk.presenceofmind.task.attributes.EventAttribute
import net.dzikoysk.presenceofmind.task.attributes.getDateAsString
import net.dzikoysk.presenceofmind.task.attributes.getTimeAsString

@Composable
fun EventConfiguration(
    task: Task,
    eventAttribute: EventAttribute,
    updateTask: UpdateTask
) {
    val currentEventDate = eventAttribute.eventDate

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, newYear, newMonth, newDayOfMonth ->
            updateTask(task.copy(
                eventAttribute = eventAttribute.copy(
                    eventDate = currentEventDate.copy(
                        year = newYear,
                        month = newMonth,
                        day = newDayOfMonth
                    )
                )
            ))
        },
        currentEventDate.year,
        currentEventDate.month,
        currentEventDate.day
    )

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute ->
            updateTask(
                task.copy(
                    eventAttribute = eventAttribute.copy(
                        eventDate = currentEventDate.copy(
                            hour = hour,
                            minute = minute
                        )
                    )
                )
            )
        },
        currentEventDate.hour,
        currentEventDate.minute,
        true
    )

    Column(Modifier.padding(12.dp)) {
        OutlinedTextField(
            value = currentEventDate.getDateAsString(),
            label = { Text(text = "Select the date of the event") },
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable { datePickerDialog.show() }
        )
        OutlinedTextField(
            value = currentEventDate.getTimeAsString(),
            label = { Text(text = "Select the time of the event") },
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(58.dp)
                .clickable { timePickerDialog.show() }
        )
    }
}