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
import net.dzikoysk.presenceofmind.shared.scaledSp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayLabel(modifier: Modifier = Modifier) {
    val todayFormatter = remember {
        DateTimeFormatter.ofPattern("E dd.MM")
            .withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault())
    }

    val fontSize = 13.scaledSp()

    Column(modifier) {
        Row {
            Text(
                text = "Today is ",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777777),
                fontSize = fontSize
            )
            Text(
                text = todayFormatter.format(Instant.now()),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = fontSize
            )
        }
        Row {
            Text(
                text = "Suggested activities for you:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777777),
                fontSize = fontSize
            )
        }
    }
}