@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.settings_theme

import androidx.annotation.ColorRes

interface SettingsThemeContract {

    interface Screen {

        fun setDarkThemeCheckBox(checked: Boolean)

        fun setSectionColor(@ColorRes sectionColorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean)
    }
}
