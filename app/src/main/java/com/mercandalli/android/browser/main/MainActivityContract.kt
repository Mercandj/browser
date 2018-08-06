package com.mercandalli.android.browser.main

internal class MainActivityContract {

    internal interface UserAction {

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
    }
}
