package net.dzikoysk.presenceofmind.model.presence

import android.content.SharedPreferences
import androidx.core.content.edit
import net.dzikoysk.presenceofmind.BuildConfig

sealed interface PresenceRepository {

    fun setColorMode(isLightMode: Boolean)

    fun isLightMode(): Boolean

    fun setLatestVersionCode(code: Int)

    fun getLatestVersionCode(): Int

}

class SharedPreferencesPresenceRepository(
    private val sharedPreferences: SharedPreferences,
    version: String
) : PresenceRepository {

    private val lightModeId = "light-mode-$version"
    private val latestVersionCodeId = "latest-version-code-$version"

    private fun setProperty(modifier: SharedPreferences.Editor.() -> Unit): Unit =
        sharedPreferences.edit(commit = true) { modifier(this) }

    override fun setColorMode(isLightMode: Boolean) = setProperty {
        putBoolean(lightModeId, isLightMode)
    }

    override fun isLightMode(): Boolean =
        sharedPreferences.getBoolean(lightModeId, true)

    override fun setLatestVersionCode(code: Int) = setProperty {
        putInt(latestVersionCodeId, code)
    }

    override fun getLatestVersionCode(): Int =
        sharedPreferences.getInt(latestVersionCodeId, BuildConfig.VERSION_CODE)

}

class InMemoryPresenceRepository : PresenceRepository {

    private var lightMode = true
    private var latestVersionCode = BuildConfig.VERSION_CODE

    override fun setColorMode(isLightMode: Boolean) {
        this.lightMode = isLightMode
    }

    override fun isLightMode(): Boolean =
        lightMode

    override fun setLatestVersionCode(code: Int) {
        latestVersionCode = code
    }

    override fun getLatestVersionCode(): Int =
        latestVersionCode

}

