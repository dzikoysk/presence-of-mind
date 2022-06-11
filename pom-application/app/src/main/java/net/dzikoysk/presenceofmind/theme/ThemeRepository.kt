package net.dzikoysk.presenceofmind.theme

import android.content.SharedPreferences
import androidx.core.content.edit

sealed interface ThemeRepository {

    fun setColorMode(isLightMode: Boolean)

    fun isLightMode(): Boolean

}

class SharedPreferencesThemeRepository(private val sharedPreferences: SharedPreferences) : ThemeRepository {

    private companion object {
        private const val LIGHT_MODE_ID = "light-mode"
    }

    override fun setColorMode(isLightMode: Boolean) {
        sharedPreferences.edit(commit = true) {
            putBoolean(LIGHT_MODE_ID, isLightMode)
        }
    }

    override fun isLightMode(): Boolean =
        sharedPreferences.getBoolean(LIGHT_MODE_ID, true)

}

class InMemoryThemeRepository : ThemeRepository {

    private var lightMode = true

    override fun setColorMode(isLightMode: Boolean) {
        this.lightMode = isLightMode
    }

    override fun isLightMode(): Boolean =
        lightMode

}

