package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.task.Task
import net.dzikoysk.presenceofmind.task.UpdateTask
import net.dzikoysk.presenceofmind.task.attributes.DayOfWeek
import net.dzikoysk.presenceofmind.task.attributes.RepetitiveAttribute
import net.dzikoysk.presenceofmind.task.attributes.RepetitiveVariant
import net.dzikoysk.presenceofmind.task.attributes.RepetitiveVariant.DAYS_OF_WEEK
import net.dzikoysk.presenceofmind.task.attributes.RepetitiveVariant.INTERVAL_IN_DAYS

@Preview(showBackground = true)
@Composable
fun RepetitiveConfigurationPreviewOfIntervalInDays() {
    RepetitiveConfiguration(
        task = Task(),
        repetitiveAttribute = RepetitiveAttribute(5),
        updateTask = {}
    )
}

@Preview(showBackground = true)
@Composable
fun RepetitiveConfigurationPreviewOfDaysOfWeek() {
    RepetitiveConfiguration(
        task = Task(),
        repetitiveAttribute = RepetitiveAttribute(daysOfWeek = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY )),
        updateTask = {}
    )
}

@Composable
fun RepetitiveConfiguration(
    task: Task,
    repetitiveAttribute: RepetitiveAttribute,
    updateTask: UpdateTask
) {
    val updateRepetitiveAttribute: (RepetitiveAttribute) -> Unit = {
        updateTask(task.copy(repetitiveAttribute = it))
    }

    val tabs = linkedMapOf(
        DAYS_OF_WEEK to {
            updateRepetitiveAttribute(RepetitiveAttribute(
                    daysOfWeek = listOf()
            ))
        },
        INTERVAL_IN_DAYS to {
            updateRepetitiveAttribute(RepetitiveAttribute(
                    intervalInDays = 0
            ))
        }
    )

    var selectedTab by remember { mutableStateOf(repetitiveAttribute.daysOfWeek?.let { 0 } ?: 1) }

    Column(Modifier.padding(12.dp)) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.keys.forEachIndexed { index, variant ->
                Tab(
                    text = { Text(variant.description) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }
        when (RepetitiveVariant.values()[selectedTab]) {
            DAYS_OF_WEEK ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    DayOfWeek.values().forEach { dayOfWeek ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(35.dp)
                                .background(MaterialTheme.colors.secondary)
                                .clickable {
                                    updateRepetitiveAttribute(RepetitiveAttribute(
                                        daysOfWeek = repetitiveAttribute.daysOfWeek
                                    ))
                                }
                        ) {
                            Text(
                                text = dayOfWeek.abbreviation,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            INTERVAL_IN_DAYS ->
                OutlinedTextField(
                    value = repetitiveAttribute.intervalInDays.takeIf { it != 0 }?.toString() ?: "",
                    label = { Text("Interval in days") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    onValueChange = {
                        updateTask(
                            task.copy(
                                repetitiveAttribute = repetitiveAttribute.copy(
                                    intervalInDays = it.toIntOrNull() ?: 0
                                )
                            )
                        )
                    }
                )
        }
    }
}