package com.mercandalli.android.browser.main

internal class MainActivityPresenter(
        private val screen: MainActivityContract.Screen
) : MainActivityContract.UserAction {

    override fun onSearchPerformed(search: String) {
        screen.showLoader(0)
        val url =
                if (search.startsWith("https://") || search.startsWith("http://")) search
                else "https://www.google.fr/search?q=" + search.replace(" ", "+")
        screen.showUrl(url)
        screen.resetSearchInput()
        screen.collapseToolbar()
        screen.hideKeyboard()
    }

    override fun onHomeClicked() {
        screen.hideLoader()
        screen.navigateHome()
    }

    override fun onClearDataClicked() {
        screen.clearData()
        screen.showClearDataMessage()
        screen.hideLoader()
        screen.navigateHome()
    }

    override fun onSettingsClicked() {
        screen.hideLoader()
        screen.navigateSettings()
    }

    override fun onPageLoadProgressChanged(progressPercent: Int) {
        if (progressPercent >= 100) {
            screen.hideLoader()
        } else {
            screen.showLoader(progressPercent)
        }
    }

    override fun onPageTouched() {
        screen.hideKeyboard()
    }

    override fun onBackPressed() {
        screen.back()
    }

    override fun onToolbarCollapsed(collapsed: Boolean) {
        screen.setToolbarContentVisible(!collapsed)
    }
}
