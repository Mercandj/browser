package com.mercandalli.android.browser.settings

import android.app.Activity
import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.search_engine.SearchEngine
import com.mercandalli.android.libs.monetization.MonetizationGraph
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class SettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr),
        SettingsContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings, this)

    private val adBlockerRow: View = view.findViewById(R.id.view_settings_ad_blocker_row)
    private val adBlockerUnlockRow: View = view.findViewById(R.id.view_settings_ad_blocker_unlock_row)
    private val adBlockerSection: CardView = view.findViewById(R.id.view_settings_ad_blocker_section)
    private val adBlockerSectionLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_section_label)
    private val adBlockerLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_label)
    private val adBlockerSubLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_sublabel)
    private val adBlockerCheckBox: CheckBox = view.findViewById(R.id.view_settings_ad_blocker)
    private val adBlockerUnlockLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_unlock_label)
    private val adBlockerUnlockSubLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_unlock_sublabel)

    private val searchEngineRow: View = view.findViewById(R.id.view_settings_search_engine_row)
    private val searchEngineUnlockRow: View = view.findViewById(R.id.view_settings_search_engine_unlock_row)
    private val searchEngineSection: CardView = view.findViewById(R.id.view_settings_search_engine_section)
    private val searchEngineSectionLabel: TextView = view.findViewById(R.id.view_settings_search_engine_section_label)
    private val searchEngineLabel: TextView = view.findViewById(R.id.view_settings_search_engine_label)
    private val searchEngineSubLabel: TextView = view.findViewById(R.id.view_settings_search_engine_sublabel)
    private val searchEngineUnlockLabel: TextView = view.findViewById(R.id.view_settings_search_engine_unlock_label)
    private val searchEngineUnlockSubLabel: TextView = view.findViewById(R.id.view_settings_search_engine_unlock_sublabel)

    private val themeRow: View = view.findViewById(R.id.view_settings_theme_row)
    private val themeSection: CardView = view.findViewById(R.id.view_settings_theme_section)
    private val themeSectionLabel: TextView = view.findViewById(R.id.view_settings_theme_section_label)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_theme_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_theme_sublabel)
    private val themeCheckBox: CheckBox = view.findViewById(R.id.view_settings_theme)

    private val aboutSection: CardView = view.findViewById(R.id.view_settings_about_section)
    private val aboutSectionLabel: TextView = view.findViewById(R.id.view_settings_about_section_label)
    private val versionNameRow: View = view.findViewById(R.id.view_settings_app_version_name_row)
    private val versionNameLabel: TextView = view.findViewById(R.id.view_settings_app_version_name_label)
    private val versionName: TextView = view.findViewById(R.id.view_settings_app_version_name)
    private val versionCodeLabel: TextView = view.findViewById(R.id.view_settings_app_version_code_label)
    private val versionCode: TextView = view.findViewById(R.id.view_settings_app_version_code)
    private val longVersionCodeLabel: TextView = view.findViewById(R.id.view_settings_app_long_version_code_label)
    private val longVersionCode: TextView = view.findViewById(R.id.view_settings_app_long_version_code)

    private val userAction = createUserAction()

    private var activityContainer: InAppManager.ActivityContainer? = null

    init {
        themeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            userAction.onDarkThemeCheckBoxCheckedChanged(isChecked)
        }
        themeRow.setOnClickListener {
            val isChecked = !themeCheckBox.isChecked
            themeCheckBox.isChecked = isChecked
            userAction.onDarkThemeCheckBoxCheckedChanged(isChecked)
        }
        adBlockerRow.setOnClickListener {
            val isChecked = !adBlockerCheckBox.isChecked
            adBlockerCheckBox.isChecked = isChecked
            userAction.onAdBlockerCheckBoxCheckedChanged(isChecked)
        }
        adBlockerUnlockRow.setOnClickListener {
            userAction.onAdBlockerUnlockRowClicked(activityContainer!!)
        }
        searchEngineRow.setOnClickListener {
            userAction.onSearchEngineRowClicked()
        }
        searchEngineUnlockRow.setOnClickListener {
            userAction.onSearchEngineUnlockRowClicked(activityContainer!!)
        }
        versionNameRow.setOnClickListener {
            userAction.onVersionNameRowClicked()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        userAction.onDetached()
    }

    override fun setDarkThemeCheckBox(checked: Boolean) {
        themeCheckBox.isChecked = checked
    }

    override fun setSectionColor(@ColorRes sectionColorRes: Int) {
        val sectionColor = ContextCompat.getColor(context, sectionColorRes)
        adBlockerSection.setCardBackgroundColor(sectionColor)
        searchEngineSection.setCardBackgroundColor(sectionColor)
        themeSection.setCardBackgroundColor(sectionColor)
        aboutSection.setCardBackgroundColor(sectionColor)
    }

    override fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textPrimaryColorRes)
        adBlockerLabel.setTextColor(textColor)
        adBlockerUnlockLabel.setTextColor(textColor)
        searchEngineLabel.setTextColor(textColor)
        searchEngineUnlockLabel.setTextColor(textColor)
        themeLabel.setTextColor(textColor)
        adBlockerCheckBox.setTextColor(textColor)
        themeLabel.setTextColor(textColor)
        themeCheckBox.setTextColor(textColor)
        versionNameLabel.setTextColor(textColor)
        versionCodeLabel.setTextColor(textColor)
        longVersionCodeLabel.setTextColor(textColor)
    }

    override fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textSecondaryColorRes)
        adBlockerSectionLabel.setTextColor(textColor)
        searchEngineSectionLabel.setTextColor(textColor)
        themeSectionLabel.setTextColor(textColor)
        aboutSectionLabel.setTextColor(textColor)
        adBlockerSubLabel.setTextColor(textColor)
        adBlockerUnlockSubLabel.setTextColor(textColor)
        searchEngineSubLabel.setTextColor(textColor)
        searchEngineUnlockSubLabel.setTextColor(textColor)
        themeSubLabel.setTextColor(textColor)
        versionName.setTextColor(textColor)
        versionCode.setTextColor(textColor)
        longVersionCode.setTextColor(textColor)
    }

    override fun setVersionName(versionName: String) {
        this.versionName.text = versionName
    }

    override fun setVersionCode(versionCode: String) {
        this.versionCode.text = versionCode
    }

    override fun setLongVersionCode(longVersionCode: String) {
        this.longVersionCode.text = longVersionCode
    }

    override fun showAdBlockerUnlockRow() {
        adBlockerUnlockRow.visibility = VISIBLE
    }

    override fun hideAdBlockerUnlockRow() {
        adBlockerUnlockRow.visibility = GONE
    }

    override fun showAdBlockerRow() {
        adBlockerRow.visibility = VISIBLE
    }

    override fun hideAdBlockerRow() {
        adBlockerRow.visibility = GONE
    }

    override fun showSearchEngineUnlockRow() {
        searchEngineUnlockRow.visibility = VISIBLE
    }

    override fun hideSearchEngineUnlockRow() {
        searchEngineUnlockRow.visibility = GONE
    }

    override fun showSearchEngineRow() {
        searchEngineRow.visibility = VISIBLE
    }

    override fun hideSearchEngineRow() {
        searchEngineRow.visibility = GONE
    }

    override fun setAdBlockerEnabled(enabled: Boolean) {
        adBlockerCheckBox.isChecked = enabled
    }

    override fun showAdBlockSection() {
        adBlockerSection.visibility = VISIBLE
    }

    override fun hideAdBlockSection() {
        adBlockerSection.visibility = GONE
    }

    override fun showAdBlockSectionLabel() {
        adBlockerSectionLabel.visibility = VISIBLE
    }

    override fun hideAdBlockSectionLabel() {
        adBlockerSectionLabel.visibility = GONE
    }

    override fun showSearchEngineSection() {
        searchEngineSection.visibility = VISIBLE
    }

    override fun hideSearchEngineSection() {
        searchEngineSection.visibility = GONE
    }

    override fun showSearchEngineSectionLabel() {
        searchEngineSectionLabel.visibility = VISIBLE
    }

    override fun hideSearchEngineSectionLabel() {
        searchEngineSectionLabel.visibility = GONE
    }

    override fun showSearchEngineSelection(searchEngines: List<SearchEngine>) {
        // TODO
    }

    override fun displaySearchEngine(searchEngineName: String) {
        searchEngineSubLabel.text = context.resources.getString(R.string.view_settings_search_engine_sublabel, searchEngineName)
    }

    override fun showSnackbar(@StringRes text: Int, duration: Int) {
        Snackbar.make((context as Activity).window.decorView.findViewById(android.R.id.content), text, duration).show()
    }

    override fun showSnackbar(text: String, duration: Int) {
        Snackbar.make((context as Activity).window.decorView.findViewById(android.R.id.content), text, duration).show()
    }

    fun setActivityContainer(activityContainer: InAppManager.ActivityContainer) {
        this.activityContainer = activityContainer
    }

    private fun createUserAction(): SettingsContract.UserAction = if (isInEditMode) {
        object : SettingsContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {}
            override fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean) {}
            override fun onAdBlockerUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {}
            override fun onSearchEngineRowClicked() {}
            override fun onSearchEngineUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {}
            override fun onVersionNameRowClicked() {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val versionManager = ApplicationGraph.getVersionManager()
        val inAppManager = MonetizationGraph.getInAppManager()
        val adBlockerManager = ApplicationGraph.getAdBlockerManager()
        val productManager = ApplicationGraph.getProductManager()
        val searchEngineManager = ApplicationGraph.getSearchEngineManager()
        val dialogManager = ApplicationGraph.getDialogManager()
        val hashManager = ApplicationGraph.getHashManager()
        val addOn = object : SettingsPresenter.AddOn {
            override fun getCurrentTimeMillis() = System.currentTimeMillis()
        }
        SettingsPresenter(
                this,
                themeManager,
                versionManager,
                inAppManager,
                adBlockerManager,
                productManager,
                searchEngineManager,
                dialogManager,
                hashManager,
                addOn
        )
    }
}