package net.dzikoysk.presenceofmind.model.presence

import android.content.SharedPreferences
import androidx.core.content.edit
import net.dzikoysk.presenceofmind.BuildConfig

private const val DEFAULT_AVATAR = "https://avatars.githubusercontent.com/u/75123628?s=200&v=4"

sealed interface PresenceRepository {

    fun setColorMode(isLightMode: Boolean)
    fun isLightMode(): Boolean

    fun setLatestVersionCode(code: Int)
    fun getLatestVersionCode(): Int

    fun setAvatarUrl(url: String)
    fun getAvatarUrl(): String

}

class SharedPreferencesPresenceRepository(
    private val sharedPreferences: SharedPreferences,
    version: String
) : PresenceRepository {

    private val lightModeId = "light-mode-$version"
    private val latestVersionCodeId = "latest-version-code-$version"
    private val avatarUrlId = "avatar-url-$version"

    private fun setProperty(modifier: SharedPreferences.Editor.() -> Unit): Unit =
        sharedPreferences.edit(commit = true) { modifier(this) }

    override fun setColorMode(isLightMode: Boolean) = setProperty { putBoolean(lightModeId, isLightMode) }
    override fun isLightMode(): Boolean = sharedPreferences.getBoolean(lightModeId, true)

    override fun setLatestVersionCode(code: Int) = setProperty { putInt(latestVersionCodeId, code) }
    override fun getLatestVersionCode(): Int = sharedPreferences.getInt(latestVersionCodeId, BuildConfig.VERSION_CODE)

    override fun setAvatarUrl(url: String) = setProperty { putString(avatarUrlId, url) }
    override fun getAvatarUrl(): String = sharedPreferences.getString(avatarUrlId, DEFAULT_AVATAR).takeUnless { it.isNullOrEmpty() }?: DEFAULT_AVATAR

}

class InMemoryPresenceRepository : PresenceRepository {

    private var lightMode = true
    private var latestVersionCode = BuildConfig.VERSION_CODE
    private var avatarUrl = DEFAULT_AVATAR

    override fun setColorMode(isLightMode: Boolean) { this.lightMode = isLightMode }
    override fun isLightMode(): Boolean = lightMode

    override fun setLatestVersionCode(code: Int) { this.latestVersionCode = code }
    override fun getLatestVersionCode(): Int = latestVersionCode

    override fun setAvatarUrl(url: String) { this.avatarUrl = url }
    override fun getAvatarUrl(): String = avatarUrl

}

