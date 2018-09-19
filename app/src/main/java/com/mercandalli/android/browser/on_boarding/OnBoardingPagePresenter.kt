package com.mercandalli.android.browser.on_boarding

import com.mercandalli.android.browser.floating.FloatingManager
import com.mercandalli.android.browser.network.NetworkManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

internal class OnBoardingPagePresenter(
        private val screen: OnBoardingPageContract.Screen,
        private val themeManager: ThemeManager,
        private val networkManager: NetworkManager,
        private val floatingManager: FloatingManager
) : OnBoardingPageContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
        if (networkManager.isNetworkAvailable()) {
            screen.showWebView()
            screen.hideWebViewPlaceholder()
        } else {
            screen.hideWebView()
            screen.showWebViewPlaceholder()
        }
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onDarkThemeClicked() {
        themeManager.setDarkEnable(true)
    }

    override fun onLightThemeClicked() {
        themeManager.setDarkEnable(false)
    }

    override fun onFullVersionTryClicked() {
        floatingManager.start("https://www.google.com/")
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        if (themeManager.isDarkEnable()) {
            screen.hideTryDarkTheme()
            screen.showLightTheme()
        } else {
            screen.showTryDarkTheme()
            screen.hideLightTheme()
        }
        screen.setCardBackgroundColorRes(theme.cardBackgroundColorRes)
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.reloadWebView()
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }
}
