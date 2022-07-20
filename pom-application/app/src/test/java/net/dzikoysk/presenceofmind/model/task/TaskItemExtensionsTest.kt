package net.dzikoysk.presenceofmind.model.task

import junit.framework.Assert.assertEquals
import net.dzikoysk.presenceofmind.pages.dashboard.list.attributes.replaceRawLinkWithMarkdown
import org.junit.Test

internal class TaskItemExtensionsTest {

    @Test
    fun `should replace raw links with markdown`() {
        val text = "Some text https://github.com/dzikoysk/presence-of-mind/tree/main#readme some text"

        val result = replaceRawLinkWithMarkdown(text)

        assertEquals(result, "Some text [github.com/dzikoysk/presence-of-mind/tree/main#readme](https://github.com/dzikoysk/presence-of-mind/tree/main#readme) some text")
    }

}