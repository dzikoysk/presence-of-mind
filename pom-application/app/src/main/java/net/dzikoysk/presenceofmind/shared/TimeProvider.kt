package net.dzikoysk.presenceofmind.shared

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}

class DefaultTimeProvider : TimeProvider {
    override fun now(): Instant = Instant.now()
}