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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.model.task.Task
import net.dzikoysk.presenceofmind.model.task.UpdateTask
import net.dzikoysk.presenceofmind.model.task.attributes.date.EventAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.toLocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EventConfiguration(
    task: Task,
    eventAttribute: EventAttribute,
    updateTask: UpdateTask
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd")}
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm")}
    val currentEventDate = eventAttribute.eventDate
    val currentEventDateTime = currentEventDate.toLocalDateTime()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, newYear, newMonth, newDayOfMonth ->
            updateTask(task.copy(
                eventAttribute = eventAttribute.copy(
                    eventDate = currentEventDate.copy(
                        year = newYear,
                        month = newMonth + 1,
                        day = newDayOfMonth
                    )
                )
            ))
        },
        currentEventDate.year,
        currentEventDate.month - 1,
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
            value = currentEventDateTime.format(dateFormatter),
            label = { Text(text = "Select the date of the event") },
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable { datePickerDialog.show() }
        )
        OutlinedTextField(
            value = currentEventDateTime.format(timeFormatter),
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