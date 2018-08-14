package com.mercandalli.android.browser.main

import android.os.Build
import com.crashlytics.android.Crashlytics
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.toast.ToastManager

internal class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val themeManager: ThemeManager
) : MainActivityContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onCreate() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onSearchPerformed(search: String) {
        val url = searchToUrl(search)
        screen.showLoader(0)
        screen.showUrl(url)
        screen.resetSearchInput()
        screen.collapseToolbar()
        screen.hideKeyboard()
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

    override fun onBackPressed() {
        screen.back()
    }

    override fun onToolbarCollapsed(collapsed: Boolean) {
        screen.setToolbarContentVisible(!collapsed)
    }

    private fun searchToUrl(search: String): String {
        return if (search.startsWith("https://") || search.startsWith("http://")) {
            search
        } else {
            "https://www.google.fr/search?q=" + search.replace(" ", "+")
        }
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        val textPrimaryColorRes = theme.textPrimaryColorRes
        val windowBackgroundColorRes = theme.windowBackgroundColorRes
        screen.setInputTextColorRes(textPrimaryColorRes)
        screen.setWindowBackgroundColorRes(windowBackgroundColorRes)
        screen.setToolbarBackgroundColorRes(windowBackgroundColorRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val statusBarBackgroundColorRes = theme.statusBarBackgroundColorRes
            screen.setStatusBarBackgroundColorRes(statusBarBackgroundColorRes)
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
            screen.reload()
        }
    }
}
