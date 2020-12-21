package com.kotlinflowdemo.data.theme

import android.content.SharedPreferences
import com.kotlinflowdemo.other.Constants
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class ThemeDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private val themeChannel: ConflatedBroadcastChannel<Theme> by lazy {
        ConflatedBroadcastChannel<Theme>().also { channel ->
            // When there is an access to theme channel
            // get the current theme from shared preferences
            // and send it to consumers
            val theme = sharedPreferences.getString(
                Constants.PREFERENCE_KEY_THEME,
                null
            ) ?: Theme.LIGHT.name // Default theme is light

            channel.offer(Theme.valueOf(theme))
        }
    }

    fun getTheme(): Flow<Theme> {
        return themeChannel.asFlow()
    }

    fun setTheme(theme: Theme) {
        // Save theme to shared preferences
        sharedPreferences
            .edit()
            .putString(Constants.PREFERENCE_KEY_THEME, theme.name)
            .apply()

        // Notify consumers
        themeChannel.offer(theme)
    }
}