package com.mercandalli.android.wear.browser.theme

import android.content.SharedPreferences

internal class ThemeManagerImpl(
    private val sharedPreferences: SharedPreferences
) : ThemeManager {

    private val lightTheme: Theme = LightTheme()
    private val darkTheme: Theme = DarkTheme()
    private var currentThemeIndex = sharedPreferences.getInt("theme", 0)
    private var listeners = ArrayList<ThemeManager.ThemeListener>()

    override fun getTheme() = if (currentThemeIndex == 0) lightTheme else darkTheme

    override fun setDarkEnable(enable: Boolean) {
        currentThemeIndex = if (enable) 1 else 0
        sharedPreferences.edit().putInt("theme", currentThemeIndex).apply()
        for (listener in listeners) {
            listener.onThemeChanged()
        }
    }

    override fun isDarkEnable() = currentThemeIndex != 0

    override fun registerThemeListener(listener: ThemeManager.ThemeListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterThemeListener(listener: ThemeManager.ThemeListener) {
        listeners.remove(listener)
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "ThemeManager"
    }
}
