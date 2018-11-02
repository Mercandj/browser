package com.mercandalli.android.browser.settings.about

import android.os.Build
import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.dialog.DialogManager
import com.mercandalli.android.browser.hash.HashManager
import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.version.VersionManager

class SettingsAboutPresenter(
    private val screen: SettingsAboutContract.Screen,
    private val themeManager: ThemeManager,
    private val versionManager: VersionManager,
    private val productManager: ProductManager,
    private val dialogManager: DialogManager,
    private val hashManager: HashManager,
    private val addOn: AddOn
) : SettingsAboutContract.UserAction {

    private val themeListener = createThemeListener()
    private val dialogListener = createDialogListener()
    private val versionClickTimestampsMs = ArrayList<Long>()

    override fun onAttached() {
        dialogManager.registerListener(dialogListener)
        themeManager.registerThemeListener(themeListener)
        updateTheme()
        setVersions()
        val dialogAction = dialogManager.consumeDialogActionPositiveClicked()
        consumeDialogActionPositiveClicked(dialogAction)
    }

    override fun onDetached() {
        dialogManager.unregisterListener(dialogListener)
        themeManager.unregisterThemeListener(themeListener)
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
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun setIsAppDeveloperEnabled(isAppDeveloperModeEnabled: Boolean) {
        productManager.setIsAppDeveloperEnabled(isAppDeveloperModeEnabled)
        screen.showSnackbar(
            if (isAppDeveloperModeEnabled) R.string.view_settings_developer_mode_enabled
            else R.string.view_settings_developer_mode_disabled,
            Snackbar.LENGTH_SHORT
        )
        versionClickTimestampsMs.clear()
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

    private fun consumeDialogActionPositiveClicked(dialogAction: DialogManager.DialogAction?) {
        if (dialogAction == null) {
            return
        }
        when (dialogAction.dialogId) {
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
                val isAppDeveloperModeEnabled = hashManager.sha256(dialogAction.userInput, 32) ==
                    "1753549de2d885325195f6ab9e3f86174f7f2626ccd3d4eccae82398b48de19d"
                setIsAppDeveloperEnabled(isAppDeveloperModeEnabled)
            }
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createDialogListener() = object : DialogManager.Listener {
        override fun onDialogPositiveClicked(dialogAction: DialogManager.DialogAction): Boolean {
            consumeDialogActionPositiveClicked(dialogAction)
            return true
        }

        override fun onDialogNegativeClicked(dialogAction: DialogManager.DialogAction) {
        }
    }

    interface AddOn {

        fun getCurrentTimeMillis(): Long
    }

    companion object {
        private const val DIALOG_ID_VERSION_NAME = "SettingsAboutPresenter.DIALOG_ID_VERSION_NAME"
        private const val DIALOG_ID_PROMPT_PASS = "SettingsAboutPresenter.DIALOG_ID_PROMPT_PASS"
    }
}
