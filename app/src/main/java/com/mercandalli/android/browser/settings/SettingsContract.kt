package com.mercandalli.android.browser.settings

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.mercandalli.android.browser.search_engine.SearchEngine
import com.mercandalli.android.browser.in_app.InAppManager

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

        fun showSearchEngineUnlockRow()

        fun hideSearchEngineUnlockRow()

        fun showSearchEngineRow()

        fun hideSearchEngineRow()

        fun showAdBlockSection()

        fun hideAdBlockSection()

        fun showAdBlockSectionLabel()

        fun hideAdBlockSectionLabel()

        fun showSearchEngineSection()

        fun hideSearchEngineSection()

        fun showSearchEngineSectionLabel()

        fun hideSearchEngineSectionLabel()

        fun setAdBlockerEnabled(enabled: Boolean)

        fun showSearchEngineSelection(searchEngines: List<SearchEngine>)

        fun displaySearchEngine(searchEngineName: String)

         fun showSnackbar(@StringRes text: Int, duration: Int)

         fun showSnackbar(text: String, duration: Int)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean)

        fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean)

        fun onAdBlockerUnlockRowClicked(activityContainer: InAppManager.ActivityContainer)

        fun onSearchEngineRowClicked()

        fun onSearchEngineUnlockRowClicked(activityContainer: InAppManager.ActivityContainer)

        fun onVersionNameRowClicked()
    }
}