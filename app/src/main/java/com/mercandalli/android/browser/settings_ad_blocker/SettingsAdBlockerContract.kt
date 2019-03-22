@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.settings_ad_blocker

import androidx.annotation.ColorRes
import com.mercandalli.android.sdk.purchase.PurchaseManager

interface SettingsAdBlockerContract {

    interface Screen {

        fun setSectionColor(@ColorRes sectionColorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int)

        fun showAdBlockerUnlockRow()

        fun hideAdBlockerUnlockRow()

        fun showAdBlockerRow()

        fun hideAdBlockerRow()

        fun showAdBlockSection()

        fun hideAdBlockSection()

        fun showAdBlockSectionLabel()

        fun hideAdBlockSectionLabel()

        fun setAdBlockerEnabled(enabled: Boolean)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean)

        fun onAdBlockerUnlockRowClicked(activityContainer: PurchaseManager.ActivityContainer)
    }
}
