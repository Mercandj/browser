package com.mercandalli.android.browser.floating

import androidx.annotation.ColorRes

interface FloatingContract {

    interface UserAction {

        fun onAttachedToWindow()

        fun onDetachedFromWindow()

        fun onQuitClicked()

        fun onFullscreenClicked(url: String)

        fun onCollapseClicked()

        fun onHomeClicked()
    }

    interface Screen {

        fun removeFromWindowManager()

        fun expand()

        fun collapse()

        fun reload()

        fun setPrimaryTextColorRes(@ColorRes colorRes: Int)

        fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int)

        fun navigateToMainActivity(url: String)

        fun isCollapsed(): Boolean

        fun loadUrl(url: String)
    }
}