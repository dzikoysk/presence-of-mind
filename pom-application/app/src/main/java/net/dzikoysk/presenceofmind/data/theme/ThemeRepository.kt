package net.dzikoysk.presenceofmind.data.theme

import android.content.SharedPreferences
import androidx.core.content.edit

sealed interface ThemeRepository {

    fun setColorMode(isLightMode: Boolean)

    fun isLightMode(): Boolean

}

class SharedPreferencesThemeRepository(
    private val sharedPreferences: SharedPreferences,
    version: String
) : ThemeRepository {

    private val lightModeId = "light-mode-$version"

    override fun setColorMode(isLightMode: Boolean) {
        sharedPreferences.edit(commit = true) {
            putBoolean(lightModeId, isLightMode)
        }
    }

    override fun isLightMode(): Boolean =
        sharedPreferences.getBoolean(lightModeId, true)

}

class InMemoryThemeRepository : ThemeRepository {

    private var lightMode = true

    override fun setColorMode(isLightMode: Boolean) {
        this.lightMode = isLightMode
    }

    override fun isLightMode(): Boolean =
        lightMode

}

