package com.mercandalli.android.browser.settings

import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi

interface SettingsActivityContract {

    interface Screen {

        fun quit()

        fun setWindowBackgroundColorRes(@ColorRes colorRes: Int)

        fun setTextDarkColorRes(@ColorRes colorRes: Int)

        fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int)

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int)

        @RequiresApi(api = Build.VERSION_CODES.M)
        fun setStatusBarDark(statusBarDark: Boolean)
    }

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onToolbarBackClicked()
    }
}