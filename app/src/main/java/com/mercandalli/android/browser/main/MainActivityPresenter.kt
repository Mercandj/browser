package com.mercandalli.android.browser.main

import android.os.Build
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

internal class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val themeManager: ThemeManager
) : MainActivityContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onCreate(firstActivityLaunch: Boolean) {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
        if (firstActivityLaunch) {
            screen.hideFab()
            screen.hideWebView()
            screen.showEmptyView()
            screen.showKeyboard()
            screen.showToolbar()
        }
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onSearchPerformed(search: String) {
        val url = searchToUrl(search)
        screen.showLoader(0)
        screen.showUrl(url)
        screen.resetSearchInput()
        screen.hideKeyboard()
        screen.showFabExpand()
        screen.showWebView()
        screen.hideEmptyView()
        screen.hideToolbar()
    }

    override fun onHomeClicked() {
        screen.hideLoader()
        screen.navigateHome()
    }

    override fun onClearDataClicked() {
        screen.clearData()
        screen.showClearDataMessage()
        screen.hideLoader()
        screen.navigateHome()
        screen.hideWebView()
        screen.showEmptyView()
        screen.showToolbar()
    }

    override fun onSettingsClicked() {
        screen.hideLoader()
        screen.navigateSettings()
    }

    override fun onPageLoadProgressChanged(progressPercent: Int) {
        if (progressPercent >= 100) {
            screen.hideLoader()
        } else {
            screen.showLoader(progressPercent)
        }
    }

    override fun onPageTouched() {
        screen.hideKeyboard()
    }

    override fun onBackPressed(emptyViewVisible: Boolean) {
        if (emptyViewVisible) {
            screen.quit()
            return
        }
        screen.back()
    }

    override fun onFabClicked(expand: Boolean) {
        screen.showToolbar()
        if (expand) {
            screen.showFabClear()
            return
        }
        screen.clearData()
        screen.showClearDataMessage()
        screen.hideLoader()
        screen.navigateHome()
        screen.hideFab()
        screen.hideWebView()
        screen.showEmptyView()
        screen.showKeyboard()
    }

    private fun searchToUrl(search: String): String {
        return if (search.startsWith("https://") || search.startsWith("http://")) {
            search
        } else {
            "https://www.google.fr/search?q=" + search.replace(" ", "+")
        }
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setInputTextColorRes(theme.textPrimaryColorRes)
        screen.setWindowBackgroundColorRes(theme.windowBackgroundColorRes)
        screen.setToolbarBackgroundColorRes(theme.toolbarBackgroundColorRes)
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
