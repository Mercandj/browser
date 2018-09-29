package com.mercandalli.android.tv.browser.theme

interface ThemeManager {

    fun getTheme(): Theme

    fun setDarkEnable(enable: Boolean)

    fun isDarkEnable(): Boolean

    fun registerThemeListener(listener: ThemeListener)

    fun unregisterThemeListener(listener: ThemeListener)

    interface ThemeListener {
        fun onThemeChanged()
    }
}