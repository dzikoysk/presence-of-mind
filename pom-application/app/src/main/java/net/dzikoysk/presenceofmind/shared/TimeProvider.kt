package net.dzikoysk.presenceofmind.shared

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

interface TimeProvider {

    fun now(): Instant

    fun nowAtDefaultZone(): ZonedDateTime =
        now().atZone(ZoneId.systemDefault())

}

class DefaultTimeProvider : TimeProvider {

    override fun now(): Instant =
        Instant.now()

    override fun nowAtDefaultZone(): ZonedDateTime =
        ZonedDateTime.now()

}