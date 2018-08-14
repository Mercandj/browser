package com.mercandalli.android.browser.settings

import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class SettingsActivityPresenter(
        private val screen: SettingsActivityContract.Screen,
        private val themeManager: ThemeManager
) : SettingsActivityContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onCreate() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onToolbarBackClicked() {
        screen.quit()
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        val windowBackgroundColorRes = theme.windowSettingsBackgroundColorRes
        val textDarkColorRes = theme.textDarkColorRes
        val toolbarBackgroundColorRes = theme.toolbarBackgroundColorRes
        screen.setWindowBackgroundColorRes(windowBackgroundColorRes)
        screen.setTextDarkColorRes(textDarkColorRes)
        screen.setToolbarBackgroundColorRes(toolbarBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }
}