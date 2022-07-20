package net.dzikoysk.presenceofmind.model.task.category

import android.content.SharedPreferences
import java.util.UUID

interface CategoryRepository {

    fun saveCategory(category: Category)

    fun findAllCategories(): Collection<Category>

}


class SharedPreferencesCategoryRepository(
    private val sharedPreferences: SharedPreferences,
    version: String
) : CategoryRepository {

    private val categoriesId = "categories-$version"

    override fun saveCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override fun findAllCategories(): Collection<Category> {
        TODO("Not yet implemented")
    }

}

class InMemoryCategoryRepository : CategoryRepository {

    private val categories = mutableMapOf<UUID, Category>()

    override fun saveCategory(category: Category) {
        categories[category.id] = category
    }

    override fun findAllCategories(): Collection<Category> =
        categories.values

}