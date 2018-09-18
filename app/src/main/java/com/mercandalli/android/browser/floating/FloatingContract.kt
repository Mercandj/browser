package com.mercandalli.android.browser.floating

import androidx.annotation.ColorRes

interface FloatingContract {

    interface UserAction {

        fun onAttachedToWindow()

        fun onDetachedFromWindow()

        fun onQuitClicked()

        fun onExpandClicked(url: String)
    }

    interface Screen {

        fun removeFromWindowManager()

        fun reload()

        fun setPrimaryTextColorRes(@ColorRes colorRes: Int)

        fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int)

        fun navigateToMainActivity(url: String)
    }
}