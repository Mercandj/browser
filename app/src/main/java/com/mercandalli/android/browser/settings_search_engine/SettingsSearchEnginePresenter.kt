@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.settings_search_engine

import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.search_engine.SearchEngine
import com.mercandalli.android.browser.search_engine.SearchEngineManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.in_app.InAppManager

class SettingsSearchEnginePresenter(
    private val screen: SettingsSearchEngineContract.Screen,
    private val themeManager: ThemeManager,
    private val productManager: ProductManager,
    private val searchEngineManager: SearchEngineManager
) : SettingsSearchEngineContract.UserAction {

    private val themeListener = createThemeListener()
    private val productManagerListener = createProductManagerListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        productManager.registerListener(productManagerListener)
        updateTheme()
        syncRows()
    }

    override fun onDetached() {
        productManager.unregisterListener(productManagerListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onSearchEngineRowClicked() {
        screen.showSearchEngineSelection(searchEngineManager.getSearchEngines())
        val searchEngineKey = searchEngineManager.getSearchEngineKey()
        when (searchEngineKey) {
            SearchEngine.SEARCH_ENGINE_GOOGLE -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_DUCK_DUCK_GO)
            SearchEngine.SEARCH_ENGINE_DUCK_DUCK_GO -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_BING)
            SearchEngine.SEARCH_ENGINE_BING -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_YAHOO)
            SearchEngine.SEARCH_ENGINE_YAHOO -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_QWANT)
            SearchEngine.SEARCH_ENGINE_QWANT -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_GOOGLE)
        }
        syncRows()
    }

    override fun onSearchEngineUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {
        productManager.purchaseFullVersion(activityContainer)
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun syncRows(
        isSearchEngineAvailable: Boolean = searchEngineManager.isFeatureAvailable(),
        isSubscribeToFullVersion: Boolean = productManager.isSubscribeToFullVersion()
    ) {
        if (isSubscribeToFullVersion) {
            screen.showSearchEngineSection()
            screen.showSearchEngineSectionLabel()
            screen.hideSearchEngineUnlockRow()
            screen.showSearchEngineRow()
            screen.displaySearchEngine(searchEngineManager.getSearchEngine().name)
            return
        }
        if (isSearchEngineAvailable) {
            screen.showSearchEngineSection()
            screen.showSearchEngineSectionLabel()
            screen.showSearchEngineUnlockRow()
            screen.hideSearchEngineRow()
        } else {
            screen.hideSearchEngineSection()
            screen.hideSearchEngineSectionLabel()
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createProductManagerListener() = object : ProductManager.Listener {
        override fun onSubscribeToFullVersionChanged() {
            syncRows()
        }
    }
}
