package com.mercandalli.android.browser.main

import android.os.Build
import android.os.Bundle
import com.mercandalli.android.browser.floating.FloatingManager
import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.search_engine.SearchEngineManager
import com.mercandalli.android.browser.suggestion.SuggestionManager
import com.mercandalli.android.browser.suggestion.Suggestions
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

internal class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val themeManager: ThemeManager,
        private val searchEngineManager: SearchEngineManager,
        private val suggestionManager: SuggestionManager,
        private val floatingManager: FloatingManager,
        private val productManager: ProductManager
) : MainActivityContract.UserAction {

    private val themeListener = createThemeListener()
    private val suggestionListener = createSuggestionListener()
    private val productListener = createProductListener()
    private var webViewVisible = false
    private var videoRadioButtonChecked = false
    private var search: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        themeManager.registerThemeListener(themeListener)
        suggestionManager.registerSuggestionListener(suggestionListener)
        productManager.registerListener(productListener)
        updateTheme()
        val firstActivityLaunch = savedInstanceState == null
        if (firstActivityLaunch) {
            screen.showKeyboard()
            screen.hideClearInput()
            setWebViewVisible(webViewVisible)
        }
        syncWithProduct()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
        suggestionManager.unregisterSuggestionListener(suggestionListener)
        productManager.unregisterListener(productListener)
    }

    override fun onNewIntent(url: String?) {
        if (url != null) {
            performSearch(url, true)
        }
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

    override fun onSearchInputChanged(search: String) {
        this.search = search
        if (search == "") {
            screen.hideClearInput()
            screen.hideSuggestions()
        } else {
            screen.showClearInput()
            suggestionManager.getSuggestion(search)
        }
    }

    override fun onSearchPerformed(search: String) {
        performSearch(search, false)
    }

    override fun onHomeClicked() {
        val url = searchEngineManager.getHomeUrl()
        performSearch(url, false)
    }

    override fun onClearDataClicked() {
        screen.showUrl(searchEngineManager.getHomeUrl())
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
        if (screen.webViewCanGoBack()) {
            screen.webViewBack()
            return
        }
        setWebViewVisible(false)
    }

    override fun onFabClearClicked() {
        screen.showUrl(searchEngineManager.getHomeUrl())
        screen.clearData()
        screen.showClearDataMessage()
        setWebViewVisible(false)
    }

    override fun onInputClearClicked() {
        screen.setInput("")
        screen.hideSuggestions()
        screen.hideClearInput()
        screen.showKeyboard()
    }

    override fun onVideoCheckedChanged(checked: Boolean) {
        videoRadioButtonChecked = checked
    }

    override fun onQuitClicked() {
        screen.quit()
    }

    override fun onSuggestionClicked(suggestion: String) {
        val search = suggestion.replace("<b>", "").replace("</b>", "")
        val url = convertSearchToUrl(search)
        screen.showUrl(url)
        screen.resetSearchInput()
        setWebViewVisible(true)
    }

    override fun onSuggestionImageClicked(suggestion: String) {
        val search = suggestion.replace("<b>", "").replace("</b>", "")
        screen.setInput(search)
    }

    private fun performSearch(search: String, forceNonFloating: Boolean) {
        val url = convertSearchToUrl(search)
        screen.resetSearchInput()
        if (!forceNonFloating && screen.isFloatingWindowChecked()) {
            floatingManager.start(FloatingManager.Configuration.default(url))
            return
        }
        screen.showUrl(url)
        setWebViewVisible(true)
    }

    private fun convertSearchToUrl(search: String) = if (videoRadioButtonChecked) {
        searchEngineManager.createSearchVideoUrl(search)
    } else {
        searchEngineManager.createSearchUrl(search)
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
            screen.hideSuggestions()
        } else {
            screen.hideFab()
            screen.showToolbar()
            screen.hideWebView()
            screen.showEmptyView()
            screen.hideLoader()
            screen.showKeyboard()
            if (search == "") {
                screen.hideSuggestions()
            }
        }
    }

    private fun syncWithProduct(isSubscribed: Boolean = productManager.isSubscribeToFullVersion()) {
        if (isSubscribed) {
            screen.showFloatingWindowCheckbox()
        } else {
            screen.hideFloatingWindowCheckbox()
        }
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setPrimaryTextColorRes(theme.textPrimaryColorRes)
        screen.setSecondaryTextColorRes(theme.textSecondaryColorRes)
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

    private fun createSuggestionListener() = object : SuggestionManager.SuggestionListener {
        override fun onSuggestionEnded(suggestions: Suggestions) {
            if (suggestions.searchInput != search) {
                return
            }
            screen.showSuggestions(suggestions.suggestions)
        }
    }

    private fun createProductListener() = object : ProductManager.Listener {
        override fun onSubscribeToFullVersionChanged() {
            syncWithProduct()
        }
    }
}
