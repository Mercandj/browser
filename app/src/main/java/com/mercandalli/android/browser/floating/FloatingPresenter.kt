package com.mercandalli.android.browser.floating

import android.os.Build
import com.mercandalli.android.browser.search_engine.SearchEngineManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class FloatingPresenter(
        private val screen: FloatingContract.Screen,
        private val themeManager: ThemeManager,
        private val searchEngineManager: SearchEngineManager
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

    override fun onFullscreenClicked(url: String) {
        screen.navigateToMainActivity(url)
        screen.removeFromWindowManager()
    }

    override fun onCollapseClicked() {
        if (screen.isCollapsed()) {
            screen.expand()
        } else {
            screen.collapse()
        }
    }

    override fun onHomeClicked() {
        val homeUrl = searchEngineManager.getHomeUrl()
        screen.loadUrl(homeUrl)
    }

    override fun onLoad(configuration: FloatingManager.Configuration) {
        screen.loadUrl(configuration.url)
        if (configuration.fullscreenButtonVisible) {
            screen.showFullscreenButton()
        } else {
            screen.hideFullscreenButton()
        }
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