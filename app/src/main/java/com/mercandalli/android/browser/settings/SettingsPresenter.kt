package com.mercandalli.android.browser.settings

import android.os.Build
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.version.VersionManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        private val themeManager: ThemeManager,
        private val versionManager: VersionManager
) : SettingsContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
        setVersions()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {
        themeManager.setDarkEnable(isChecked)
    }

    private fun setVersions() {
        val versionName = versionManager.getVersionName()
        val versionCode = versionManager.getVersionCode()
        val longVersionCode = if (Build.VERSION.SDK_INT >= 28) {
            versionManager.getLongVersionCode()
        } else {
            -1
        }
        screen.setVersionName(versionName)
        screen.setVersionCode(versionCode)
        screen.setLongVersionCode(longVersionCode)
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setDarkThemeCheckBox(themeManager.isDarkEnable())
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun createThemeListener(): ThemeManager.ThemeListener {
        return object : ThemeManager.ThemeListener {
            override fun onThemeChanged() {
                updateTheme()
            }
        }
    }
}