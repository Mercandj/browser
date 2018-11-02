package com.mercandalli.android.browser.settings.about

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface SettingsAboutContract {

    interface Screen {

        fun setSectionColor(@ColorRes sectionColorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int)

        fun setVersionName(versionName: String)

        fun setVersionCode(versionCode: String)

        fun setLongVersionCode(longVersionCode: String)

        fun showSnackbar(@StringRes text: Int, duration: Int)

        fun showSnackbar(text: String, duration: Int)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onVersionNameRowClicked()
    }
}
