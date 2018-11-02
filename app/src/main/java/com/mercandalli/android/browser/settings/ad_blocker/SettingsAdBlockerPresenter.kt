@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.settings.ad_blocker

import com.mercandalli.android.browser.ad_blocker.AdBlockerManager
import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.in_app.InAppManager

class SettingsAdBlockerPresenter(
    private val screen: SettingsAdBlockerContract.Screen,
    private val themeManager: ThemeManager,
    private val adBlockerManager: AdBlockerManager,
    private val productManager: ProductManager
) : SettingsAdBlockerContract.UserAction {

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

    override fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean) {
        adBlockerManager.setEnabled(isChecked)
        syncRows()
    }

    override fun onAdBlockerUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {
        productManager.purchaseFullVersion(activityContainer)
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun syncRows(
        isAdBlockAvailable: Boolean = adBlockerManager.isFeatureAvailable(),
        isSubscribeToFullVersion: Boolean = productManager.isSubscribeToFullVersion(),
        isEnabled: Boolean = adBlockerManager.isEnabled()
    ) {
        if (isSubscribeToFullVersion) {
            screen.showAdBlockSection()
            screen.showAdBlockSectionLabel()
            screen.hideAdBlockerUnlockRow()
            screen.showAdBlockerRow()
            screen.setAdBlockerEnabled(isEnabled)
            return
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

    private fun createProductManagerListener() = object : ProductManager.Listener {
        override fun onSubscribeToFullVersionChanged() {
            syncRows()
        }
    }
}
