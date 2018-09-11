package com.mercandalli.android.browser.settings

import android.os.Build
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.mercandalli.android.browser.main.MainApplication
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.version.VersionManager
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        private val themeManager: ThemeManager,
        private val versionManager: VersionManager,
        private val inAppManager: InAppManager
) : SettingsContract.UserAction {

    private val themeListener = createThemeListener()
    private val inAppManagerListener = createInAppManagerListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        inAppManager.registerListener(inAppManagerListener)
        updateTheme()
        setVersions()
        syncAdBlockerRows()
    }

    override fun onDetached() {
        inAppManager.unregisterListener(inAppManagerListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {
        themeManager.setDarkEnable(isChecked)
    }

    override fun onUnlockAdsBlocker(activityContainer: InAppManager.ActivityContainer) {
        inAppManager.purchase(
                activityContainer,
                MainApplication.SKU_SUBSCRIPTION_ADS_BLOCKER,
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

    private fun syncAdBlockerRows(isPurchased: Boolean = inAppManager.isPurchased(MainApplication.SKU_SUBSCRIPTION_ADS_BLOCKER)) {
        if (isPurchased) {
            screen.hideAdBlockerUnlockRow()
            screen.showAdBlockerRow()
        } else {
            screen.showAdBlockerUnlockRow()
            screen.hideAdBlockerRow()
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
            syncAdBlockerRows()
        }
    }
}