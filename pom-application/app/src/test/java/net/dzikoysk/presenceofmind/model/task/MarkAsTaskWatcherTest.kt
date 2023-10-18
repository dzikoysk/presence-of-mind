package net.dzikoysk.presenceofmind.model.task

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import net.dzikoysk.presenceofmind.model.task.attributes.date.RepetitiveAttribute
import net.dzikoysk.presenceofmind.model.task.attributes.date.MarkAsTaskWatcher
import net.dzikoysk.presenceofmind.shared.TimeProvider
import org.junit.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.days

class MarkAsTaskWatcherTest {

    private val firstDayOfWeekEver = Instant.ofEpochMilli(0).atZone(ZoneId.systemDefault()).dayOfWeek
    private val weekLater = Instant.ofEpochMilli(7.days.inWholeMilliseconds)

    private val taskService = TaskService(
        taskRepository = InMemoryTaskRepository(),
        taskWatchers = listOf(
            MarkAsTaskWatcher(
                timeProvider = object : TimeProvider {
                    override fun now(): Instant = weekLater
                }
            )
        )
    )

    @Test
    fun `should mark repetitive task with interval as unfinished`() {
        // given: a done task in repository with 7-day long interval
        val task = Task(
            doneDate = 0,
            repetitiveAttribute = RepetitiveAttribute(
                intervalInDays = 7
            )
        )
        taskService.saveTask(task)
        assertTrue(taskService.findTaskById(task.id)!!.isDone())

        // when: task state is refreshed
        taskService.refreshTasksState()

        // then: task should be marked as unfinished
        assertFalse(taskService.findTaskById(task.id)!!.isDone())
    }

    @Test
    fun `should mark repetitive task as unfinished by selected days of week`() {
        // given: a done task in repository
        val task = Task(
            doneDate = 0,
            repetitiveAttribute = RepetitiveAttribute(
                daysOfWeek = listOf(firstDayOfWeekEver)
            )
        )
        taskService.saveTask(task)
        assertTrue(taskService.findTaskById(task.id)!!.isDone())

        // when: task state is refreshed
        taskService.refreshTasksState()

        // then: task should be marked as unfinished
        assertFalse(taskService.findTaskById(task.id)!!.isDone())
    }

    @Test
    fun `should mark repetitive task as finished by not selected days of week`() {
        // given: an unfinished task
        val task = Task(
            repetitiveAttribute = RepetitiveAttribute(
                daysOfWeek = listOf(DayOfWeek.values()[firstDayOfWeekEver.value + 1]) // next day
            )
        )
        taskService.saveTask(task)
        assertFalse(taskService.findTaskById(task.id)!!.isDone())

        // when: task state is refreshed
        taskService.refreshTasksState()

        // then: task should be marked as finished
        assertTrue(taskService.findTaskById(task.id)!!.isDone())
    }

}