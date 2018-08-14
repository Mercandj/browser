package com.mercandalli.android.browser.main

import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.RequiresApi

internal class MainActivityContract {

    internal interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onSearchPerformed(search: String)

        fun onHomeClicked()

        fun onClearDataClicked()

        fun onSettingsClicked()

        fun onPageLoadProgressChanged(progressPercent: Int)

        fun onPageTouched()

        fun onBackPressed()

        fun onToolbarCollapsed(collapsed: Boolean)
    }

    internal interface Screen {

        fun showUrl(url: String)

        fun reload()

        fun back()

        fun navigateHome()

        fun navigateSettings()

        fun clearData()

        fun showClearDataMessage()

        fun showLoader(progressPercent: Int)

        fun hideLoader()

        fun hideKeyboard()

        fun collapseToolbar()

        fun resetSearchInput()

        fun setToolbarContentVisible(visible: Boolean)

        fun setWindowBackgroundColorRes(@ColorRes colorRes: Int)

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int)

        fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int)

        fun setInputTextColorRes(@ColorRes colorRes: Int)
    }
}
