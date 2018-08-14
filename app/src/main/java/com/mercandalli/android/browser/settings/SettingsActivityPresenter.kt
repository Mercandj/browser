package com.mercandalli.android.browser.settings

class SettingsActivityPresenter(
        private val screen: SettingsActivityContract.Screen
) : SettingsActivityContract.UserAction {

    override fun onToolbarBackClicked() {
        screen.quit()
    }
}