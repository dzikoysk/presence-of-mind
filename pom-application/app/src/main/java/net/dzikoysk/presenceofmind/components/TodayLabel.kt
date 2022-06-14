package net.dzikoysk.presenceofmind

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayLabel(modifier: Modifier = Modifier) {
    val todayFormatter = remember {
        DateTimeFormatter.ofPattern("E dd.MM")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    Column(modifier) {
        Row {
            Text(
                text = "Today is ",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777777),
                fontSize = 15.sp
            )
            Text(
                text = todayFormatter.format(Instant.now()),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 15.sp
            )
        }
        Row {
            Text(
                text = "Suggested activities for you:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777777),
                fontSize = 15.sp
            )
        }
    }
}