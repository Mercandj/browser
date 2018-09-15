package com.mercandalli.android.browser.main

import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi

internal class MainActivityContract {

    internal interface UserAction {

        fun onCreate(savedInstanceState: Bundle?)

        fun onDestroy()

        fun onResume()

        fun onSaveInstanceState(outState: Bundle)

        fun onRestoreInstanceState(outState: Bundle)

        fun onSearchPerformed(search: String)

        fun onHomeClicked()

        fun onClearDataClicked()

        fun onSettingsClicked()

        fun onPageLoadProgressChanged(progressPercent: Int)

        fun onPageTouched()

        fun onBackPressed(emptyViewVisible: Boolean)

        fun onFabClearClicked()

        fun onVideoCheckedChanged(checked: Boolean)
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

        @RequiresApi(api = Build.VERSION_CODES.M)
        fun setStatusBarDark(statusBarDark: Boolean)

        fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int)

        fun setInputTextColorRes(@ColorRes colorRes: Int)

        fun showFab()

        fun hideFab()

        fun showWebView()

        fun hideWebView()

        fun showEmptyView()

        fun hideEmptyView()

        fun showToolbar()

        fun hideToolbar()
    }
}
