package com.mercandalli.android.browser.settings

import androidx.annotation.ColorRes

interface SettingsContract {

    interface Screen {

        fun setDarkThemeCheckBox(checked: Boolean)

        fun setSectionColor(@ColorRes sectionColorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int)

        fun setVersionName(versionName: String)

        fun setVersionCode(versionCode: String)

        fun setLongVersionCode(longVersionCode: String)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean)
    }
}