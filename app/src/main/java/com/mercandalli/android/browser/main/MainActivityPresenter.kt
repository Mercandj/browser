package com.mercandalli.android.browser.main

import android.os.Build
import android.os.Bundle
import com.mercandalli.android.browser.search_engine.SearchEngineManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

internal class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val themeManager: ThemeManager,
        private val searchEngineManager: SearchEngineManager
) : MainActivityContract.UserAction {

    private val themeListener = createThemeListener()
    private var webViewVisible = false
    private var videoRadioButtonChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
        val firstActivityLaunch = savedInstanceState == null
        if (firstActivityLaunch) {
            screen.showKeyboard()
            setWebViewVisible(webViewVisible)
        }
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onResume() {
        if (!webViewVisible) {
            screen.showKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("webViewVisible", webViewVisible)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        webViewVisible = outState.getBoolean("webViewVisible")
        setWebViewVisible(webViewVisible)
    }

    override fun onSearchPerformed(search: String) {
        val url = searchToUrl(search)
        screen.showUrl(url)
        screen.resetSearchInput()
        setWebViewVisible(true)
    }

    override fun onHomeClicked() {
        setWebViewVisible(false)
    }

    override fun onClearDataClicked() {
        screen.clearData()
        screen.showClearDataMessage()
        setWebViewVisible(false)
    }

    override fun onSettingsClicked() {
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

    override fun onFabClearClicked() {
        screen.clearData()
        screen.showClearDataMessage()
        setWebViewVisible(false)
    }

    override fun onVideoCheckedChanged(checked: Boolean) {
        videoRadioButtonChecked = checked
    }

    override fun onQuitClicked() {
        screen.quit()
    }

    private fun searchToUrl(search: String): String {
        return if (videoRadioButtonChecked) {
            searchEngineManager.createSearchVideoUrl(search)
        } else {
            searchEngineManager.createSearchUrl(search)
        }
    }

    private fun setWebViewVisible(visible: Boolean) {
        webViewVisible = visible
        if (visible) {
            screen.showFab()
            screen.hideToolbar()
            screen.showWebView()
            screen.hideEmptyView()
            screen.showLoader(0)
            screen.hideKeyboard()
        } else {
            screen.hideFab()
            screen.showToolbar()
            screen.hideWebView()
            screen.showEmptyView()
            screen.hideLoader()
            screen.showKeyboard()
        }
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setPrimaryTextColorRes(theme.textPrimaryColorRes)
        screen.setAccentTextColorRes(theme.textAccentColorRes)
        screen.setWindowBackgroundColorRes(theme.windowBackgroundColorRes)
        screen.setToolbarBackgroundColorRes(theme.toolbarBackgroundColorRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screen.setStatusBarBackgroundColorRes(theme.statusBarBackgroundColorRes)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            screen.setStatusBarDark(theme.statusBarDark)
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
            screen.reload()
        }
    }
}
