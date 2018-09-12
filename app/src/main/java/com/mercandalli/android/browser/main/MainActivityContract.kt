package com.mercandalli.android.browser.main

import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi

internal class MainActivityContract {

    internal interface UserAction {

        fun onCreate(firstActivityLaunch: Boolean)

        fun onDestroy()

        fun onSearchPerformed(search: String)

        fun onHomeClicked()

        fun onClearDataClicked()

        fun onSettingsClicked()

        fun onPageLoadProgressChanged(progressPercent: Int)

        fun onPageTouched()

        fun onBackPressed(emptyViewVisible: Boolean)

        fun onFabClicked(expand: Boolean)
    }

    internal interface Screen {

        fun showUrl(url: String)

        fun reload()

        fun back()

        fun quit()

        fun navigateHome()

        fun navigateSettings()

        fun clearData()

        fun showClearDataMessage()

        fun showLoader(progressPercent: Int)

        fun hideLoader()

        fun showKeyboard()

        fun hideKeyboard()

        fun resetSearchInput()

        fun setWindowBackgroundColorRes(@ColorRes colorRes: Int)

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int)

        fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int)

        fun setInputTextColorRes(@ColorRes colorRes: Int)

        fun showFabClear()

        fun showFabExpand()

        fun hideFab()

        fun showWebView()

        fun hideWebView()

        fun showEmptyView()

        fun hideEmptyView()

        fun showToolbar()

        fun hideToolbar()
    }
}
