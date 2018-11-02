@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.on_boarding

import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import com.mercandalli.android.browser.in_app.InAppManager

internal interface OnBoardingContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onPageChanged()

        fun onNextClicked()

        fun onStoreBuyClicked(activityContainer: InAppManager.ActivityContainer)

        fun onStoreSkipClicked()

        fun onSwipeOutAtEnd(activityContainer: InAppManager.ActivityContainer)
    }

    interface Screen {

        @IntRange(from = 0)
        fun getPage(): Int

        fun setPage(@IntRange(from = 0) page: Int)

        @IntRange(from = 0)
        fun getPageCount(): Int

        fun enableStorePage()

        fun disableStorePage()

        fun showNextButton()

        fun hideNextButton()

        fun showStoreButtons()

        fun hideStoreButtons()

        fun closeOnBoarding()

        fun startFistActivity()

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setPageIndicatorDarkTheme(darkEnabled: Boolean)
    }
}
