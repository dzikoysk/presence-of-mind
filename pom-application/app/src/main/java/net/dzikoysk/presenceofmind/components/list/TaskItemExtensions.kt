package net.dzikoysk.presenceofmind.components.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import dev.jeziellago.compose.markdowntext.MarkdownText
import net.dzikoysk.presenceofmind.shared.scaledSp
import java.lang.Integer.max
import java.util.StringTokenizer

internal val SCALE_AUTOMATICALLY: TextUnit? = null

@Composable
fun DescriptionMarkdown(
    modifier: Modifier = Modifier,
    description: String,
    fontSize: TextUnit? = SCALE_AUTOMATICALLY
) {
    MarkdownText(
        modifier = modifier,
        markdown = replaceRawLinkWithMarkdown(description),
        fontSize = fontSize ?: description.scaledFontSize()
    )
}

@Composable
fun String.scaledFontSize(): TextUnit =
    max(10, 12 - (length / 20)).scaledSp()

internal fun replaceRawLinkWithMarkdown(descriptionToProcess: String): String {
    val description = StringBuilder()

    descriptionToProcess.lines().forEach { line ->
        val tokenizer = StringTokenizer(line)

        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken(" ")

            val phrase = when {
                token.trim().startsWith("https") -> "[${token.removeProtocolFromUrl()}]($token)"
                else -> token
            }

            description.append(phrase).append(" ")
        }

        description.append(System.lineSeparator())
    }

    return description.toString().trim()
}

private fun String.removeProtocolFromUrl(): String =
    replaceFirst("https://", "http://").replaceFirst("http://", "")