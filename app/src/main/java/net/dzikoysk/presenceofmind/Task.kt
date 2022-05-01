package net.dzikoysk.presenceofmind

import java.util.concurrent.atomic.AtomicLong

data class Task(
    val id: Long = TASK_ID_ASSIGNER.incrementAndGet(),
    val title: String
) {

    companion object {
        val TASK_ID_ASSIGNER = AtomicLong()
    }

}