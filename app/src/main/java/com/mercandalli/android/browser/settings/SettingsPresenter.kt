package com.mercandalli.android.browser.settings

import android.os.Build
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.ad_blocker.AdBlockerManager
import com.mercandalli.android.browser.dialog.DialogManager
import com.mercandalli.android.browser.hash.HashManager
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
        private val searchEngineManager: SearchEngineManager,
        private val dialogManager: DialogManager,
        private val hashManager: HashManager,
        private val addOn: AddOn
) : SettingsContract.UserAction {

    private val themeListener = createThemeListener()
    private val inAppManagerListener = createInAppManagerListener()
    private val dialogListener = createDialogListener()
    private val versionClickTimestampsMs = ArrayList<Long>()

    override fun onAttached() {
        dialogManager.registerListener(dialogListener)
        themeManager.registerThemeListener(themeListener)
        inAppManager.registerListener(inAppManagerListener)
        updateTheme()
        setVersions()
        syncRows()
    }

    override fun onDetached() {
        dialogManager.unregisterListener(dialogListener)
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
            SearchEngine.SEARCH_ENGINE_GOOGLE -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_DUCK_DUCK_GO)
            SearchEngine.SEARCH_ENGINE_DUCK_DUCK_GO -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_BING)
            SearchEngine.SEARCH_ENGINE_BING -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_YAHOO)
            SearchEngine.SEARCH_ENGINE_YAHOO -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_QWANT)
            SearchEngine.SEARCH_ENGINE_QWANT -> searchEngineManager.setSearchEngineKey(SearchEngine.SEARCH_ENGINE_GOOGLE)
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

    override fun onVersionNameRowClicked() {
        val currentTimeMillis = addOn.getCurrentTimeMillis()
        versionClickTimestampsMs.add(currentTimeMillis)
        if (!isEnoughVersionClick(versionClickTimestampsMs, currentTimeMillis, 5, 1000)) {
            return
        }
        val appDeveloperEnabled = productManager.isAppDeveloperEnabled()
        if (appDeveloperEnabled) {
            setIsAppDeveloperEnabled(false)
            return
        }
        dialogManager.registerListener(dialogListener)
        dialogManager.alert(
                DIALOG_ID_VERSION_NAME,
                R.string.view_settings_developer_activation_message_title,
                R.string.view_settings_developer_activation_message,
                R.string.view_settings_developer_activation_message_positive,
                R.string.view_settings_developer_activation_message_negative
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
            screen.setAdBlockerEnabled(isEnabled)

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

    private fun createDialogListener() = object : DialogManager.Listener {
        override fun onDialogPositiveClicked(dialogId: String, userInput: String) {
            when (dialogId) {
                DIALOG_ID_VERSION_NAME -> {
                    dialogManager.prompt(
                            DIALOG_ID_PROMPT_PASS,
                            R.string.view_settings_developer_activation_message_title,
                            R.string.view_settings_developer_activation_password,
                            R.string.view_settings_developer_activation_ok,
                            R.string.view_settings_developer_activation_cancel
                    )
                }
                DIALOG_ID_PROMPT_PASS -> {
                    val isAppDeveloperModeEnabled = hashManager.sha256(userInput, 32) ==
                            "4fa93ff20105d1bd93f79c2db51aa1169265130ca810e72e85b3277e92f53820"
                    setIsAppDeveloperEnabled(isAppDeveloperModeEnabled)
                }
            }
        }

        override fun onDialogNegativeClicked(dialogId: String) {

        }
    }

    private fun setIsAppDeveloperEnabled(isAppDeveloperModeEnabled: Boolean) {
        productManager.setIsAppDeveloperEnabled(isAppDeveloperModeEnabled)
        screen.showSnackbar(
                if (isAppDeveloperModeEnabled) R.string.view_settings_developer_mode_enabled
                else R.string.view_settings_developer_mode_disabled,
                Snackbar.LENGTH_SHORT
        )
        versionClickTimestampsMs.clear()
        syncRows()
    }

    private fun isEnoughVersionClick(
            timestamps: List<Long>,
            currentTimestamp: Long,
            nbClick: Int,
            duration: Long
    ): Boolean {
        if (timestamps.size < nbClick) {
            return false
        }
        return currentTimestamp < timestamps[timestamps.size - nbClick] + duration
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

    interface AddOn {

        fun getCurrentTimeMillis(): Long
    }

    companion object {
        private const val DIALOG_ID_VERSION_NAME = "DIALOG_ID_VERSION_NAME"
        private const val DIALOG_ID_PROMPT_PASS = "DIALOG_ID_PROMPT_PASS"
    }
}