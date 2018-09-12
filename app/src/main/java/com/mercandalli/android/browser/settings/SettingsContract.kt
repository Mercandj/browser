package com.mercandalli.android.browser.settings

import androidx.annotation.ColorRes
import com.mercandalli.android.libs.monetization.in_app.InAppManager

interface SettingsContract {

    interface Screen {

        fun setDarkThemeCheckBox(checked: Boolean)

        fun setSectionColor(@ColorRes sectionColorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int)

        fun setVersionName(versionName: String)

        fun setVersionCode(versionCode: String)

        fun setLongVersionCode(longVersionCode: String)

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

        fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean)

        fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean)

        fun onUnlockAdsBlocker(activityContainer: InAppManager.ActivityContainer)
    }
}