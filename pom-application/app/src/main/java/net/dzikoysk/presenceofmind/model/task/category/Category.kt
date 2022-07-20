package net.dzikoysk.presenceofmind.model.task.category

import java.util.UUID

data class Category(
    val id: UUID = UUID.randomUUID(),
    val name: String
)