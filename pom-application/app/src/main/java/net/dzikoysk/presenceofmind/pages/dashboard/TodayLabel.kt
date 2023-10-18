package net.dzikoysk.presenceofmind.pages.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import net.dzikoysk.presenceofmind.components.scaledSp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
@Preview(showBackground = true)
fun TodayLabel(modifier: Modifier = Modifier) {
    val todayFormatter = remember {
        DateTimeFormatter.ofPattern("E dd.MM")
            .withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault())
    }

    val style = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 13.scaledSp()
    )

    Column(modifier) {
        Text(
            buildAnnotatedString {
                withStyle(style.copy(color = Color(0xFF777777))) {
                    append("Today is ")
                }
                withStyle(style.copy(textDecoration = TextDecoration.Underline)) {
                    append(todayFormatter.format(Instant.now()))
                }
            }
        )
        Row {
            Text(
                AnnotatedString(
                    "Suggested activities for you:",
                    style.copy(color = Color(0xFF777777))
                )
            )
        }
    }
}
