package com.mercandalli.android.wear.browser.theme

import android.content.Context
import com.mercandalli.android.wear.browser.theme.ThemeManager
import com.mercandalli.android.wear.browser.theme.ThemeManagerImpl

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