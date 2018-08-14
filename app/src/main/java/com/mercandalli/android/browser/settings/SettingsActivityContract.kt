package com.mercandalli.android.browser.settings

import android.support.annotation.ColorRes

interface SettingsActivityContract {

    interface Screen {

        fun quit()

        fun setWindowBackgroundColorRes(@ColorRes colorRes: Int)

        fun setTextDarkColorRes(@ColorRes colorRes: Int)

        fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int)
    }

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onToolbarBackClicked()
    }
}