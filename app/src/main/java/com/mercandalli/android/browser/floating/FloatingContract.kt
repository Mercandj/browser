package com.mercandalli.android.browser.floating

interface FloatingContract {

    interface UserAction {

        fun onQuitClicked()
    }

    interface Screen {

        fun removeFromWindowManager()
    }
}