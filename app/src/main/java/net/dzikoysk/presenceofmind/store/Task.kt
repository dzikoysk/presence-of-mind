package net.dzikoysk.presenceofmind.store

import java.util.concurrent.atomic.AtomicLong

data class Task(
    val id: Long = TASK_ID_ASSIGNER.incrementAndGet(),
    val description: String
) {

    companion object {
        val TASK_ID_ASSIGNER = AtomicLong()
    }

}