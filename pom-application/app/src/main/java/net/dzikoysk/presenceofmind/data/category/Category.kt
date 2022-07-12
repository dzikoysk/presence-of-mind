package net.dzikoysk.presenceofmind.data.category

import java.util.UUID

data class Category(
    val id: UUID = UUID.randomUUID(),
    val name: String
)