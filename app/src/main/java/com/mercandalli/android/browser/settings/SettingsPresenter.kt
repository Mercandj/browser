package com.mercandalli.android.browser.settings

import com.mercandalli.android.browser.theme.ThemeManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        private val themeManager: ThemeManager
) : SettingsContract.UserAction {

    override fun onAttached() {

    }

    override fun onDetached() {

    }

    override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {

    }
}