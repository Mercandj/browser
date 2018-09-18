package com.mercandalli.android.browser.floating

import android.os.Build
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class FloatingPresenter(
        private val screen: FloatingContract.Screen,
        private val themeManager: ThemeManager
) : FloatingContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttachedToWindow() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetachedFromWindow() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onQuitClicked() {
        screen.removeFromWindowManager()
    }

    override fun onExpandClicked(url: String) {
        screen.navigateToMainActivity(url)
        screen.removeFromWindowManager()
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setPrimaryTextColorRes(theme.textPrimaryColorRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screen.setStatusBarBackgroundColorRes(theme.statusBarBackgroundColorRes)
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
            screen.reload()
        }
    }
}