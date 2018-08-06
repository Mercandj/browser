package com.mercandalli.android.browser.theme

import android.content.Context

class ThemeModule(
        private val context: Context
) {

    fun provideThemeManager(): ThemeManager {
        val sharedPreferences = context.getSharedPreferences(
                ThemeManagerImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        )
        return ThemeManagerImpl(sharedPreferences)
    }
}