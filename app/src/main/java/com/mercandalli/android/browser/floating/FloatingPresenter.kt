package com.mercandalli.android.browser.floating

class FloatingPresenter(
        private val screen: FloatingContract.Screen
) : FloatingContract.UserAction {

    override fun onQuitClicked() {
        screen.removeFromWindowManager()
    }
}