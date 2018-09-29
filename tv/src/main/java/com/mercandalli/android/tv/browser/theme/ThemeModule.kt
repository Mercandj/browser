package com.mercandalli.android.tv.browser.theme

import android.content.Context

class ThemeModule(
        private val context: Context
) {

    fun createThemeManager(): ThemeManager {
        val sharedPreferences = context.getSharedPreferences(
                ThemeManagerImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        )
        return ThemeManagerImpl(
                sharedPreferences
        )
    }
}