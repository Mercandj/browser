package com.mercandalli.android.browser.on_boarding

import androidx.annotation.ColorRes

internal interface OnBoardingPageContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onDarkThemeClicked()

        fun onLightThemeClicked()
    }

    interface Screen {

        fun reloadWebView()

        fun showTryDarkTheme()

        fun hideTryDarkTheme()

        fun showLightTheme()

        fun hideLightTheme()

        fun setCardBackgroundColorRes(@ColorRes colorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun showWebView()

        fun hideWebView()

        fun showWebViewPlaceholder()

        fun hideWebViewPlaceholder()
    }
}