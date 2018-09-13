package com.mercandalli.android.browser.settings

import android.os.Build
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.mercandalli.android.browser.ad_blocker.AdBlockerManager
import com.mercandalli.android.browser.main.MainApplication
import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.search_engine.SearchEngine
import com.mercandalli.android.browser.search_engine.SearchEngineManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.version.VersionManager
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        private val themeManager: ThemeManager,
        private val versionManager: VersionManager,
        private val inAppManager: InAppManager,
        private val adBlockerManager: AdBlockerManager,
        private val productManager: ProductManager,
        private val searchEngineManager: SearchEngineManager
) : SettingsContract.UserAction {

    private val themeListener = createThemeListener()
    private val inAppManagerListener = createInAppManagerListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        inAppManager.registerListener(inAppManagerListener)
        updateTheme()
        setVersions()
        syncRows()
    }

    override fun onDetached() {
        inAppManager.unregisterListener(inAppManagerListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {
        themeManager.setDarkEnable(isChecked)
    }

    override fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean) {
        adBlockerManager.setEnabled(isChecked)
        syncRows()
    }

    override fun onAdBlockerUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {
        inAppManager.purchase(
                activityContainer,
                MainApplication.SKU_SUBSCRIPTION_FULL_VERSION,
                BillingClient.SkuType.SUBS
        )
    }

    override fun onSearchEngineRowClicked() {
        screen.showSearchEngineSelection(searchEngineManager.getSearchEngines())

        val searchEngineKey = searchEngineManager.getSearchEngineKey()
        when (searchEngineKey) {
            SearchEngine.SEARCH_ENGINE_GOOGLE -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_YOUTUBE)
            SearchEngine.SEARCH_ENGINE_YOUTUBE -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_DUCK_DUCK_GO)
            SearchEngine.SEARCH_ENGINE_DUCK_DUCK_GO -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_GOOGLE)
        }
        syncRows()
    }

    override fun onSearchEngineUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {
        inAppManager.purchase(
                activityContainer,
                MainApplication.SKU_SUBSCRIPTION_FULL_VERSION,
                BillingClient.SkuType.SUBS
        )
    }

    private fun setVersions() {
        val buildConfigVersionName = versionManager.getBuildConfigVersionName()
        val buildConfigVersionCode = versionManager.getBuildConfigVersionCode()
        val packageManagerVersionName = versionManager.getPackageManagerVersionName()
        val packageManagerVersionCode = versionManager.getPackageManagerVersionCode()
        val packageManagerLongVersionCode = if (Build.VERSION.SDK_INT >= 28) {
            versionManager.getPackageManagerLongVersionCode()
        } else {
            -1
        }
        screen.setVersionName("BuildConfig: $buildConfigVersionName\nPackageManager: $packageManagerVersionName")
        screen.setVersionCode("BuildConfig: $buildConfigVersionCode\nPackageManager: $packageManagerVersionCode")
        screen.setLongVersionCode(packageManagerLongVersionCode.toString())
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setDarkThemeCheckBox(themeManager.isDarkEnable())
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun syncRows(
            isAdBlockAvailable: Boolean = adBlockerManager.isFeatureAvailable(),
            isSearchEngineAvailable: Boolean = searchEngineManager.isFeatureAvailable(),
            isSubscribeToFullVersion: Boolean = productManager.isSubscribeToFullVersion(),
            isEnabled: Boolean = adBlockerManager.isEnabled()
    ) {
        if (isSubscribeToFullVersion) {
            screen.showAdBlockSection()
            screen.showAdBlockSectionLabel()
            screen.hideAdBlockerUnlockRow()
            screen.showAdBlockerRow()
            screen.hideSearchEngineUnlockRow()
            screen.showSearchEngineRow()
            screen.setAdBlockerEnabled(isEnabled)
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
        if (isAdBlockAvailable) {
            screen.showAdBlockSection()
            screen.showAdBlockSectionLabel()
            screen.showAdBlockerUnlockRow()
            screen.hideAdBlockerRow()
        } else {
            screen.hideAdBlockSection()
            screen.hideAdBlockSectionLabel()
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createInAppManagerListener() = object : InAppManager.Listener {
        override fun onSkuDetailsChanged(skuDetails: SkuDetails) {

        }

        override fun onPurchasedChanged() {
            syncRows()
        }
    }
}