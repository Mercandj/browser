package com.mercandalli.android.browser.settings

interface SettingsActivityContract {

    interface Screen {

        fun quit()
    }

    interface UserAction {

        fun onToolbarBackClicked()
    }
}