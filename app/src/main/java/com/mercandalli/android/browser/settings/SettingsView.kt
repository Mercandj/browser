package com.mercandalli.android.browser.settings

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
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

    private val themeRow: View = view.findViewById(R.id.view_settings_theme_row)
    private val themeSection: CardView = view.findViewById(R.id.view_settings_theme_section)
    private val themeSectionLabel: TextView = view.findViewById(R.id.view_settings_theme_section_label)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_theme_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_theme_sublabel)
    private val themeCheckBox: CheckBox = view.findViewById(R.id.view_settings_theme)

    private val aboutSection: CardView = view.findViewById(R.id.view_settings_about_section)
    private val aboutSectionLabel: TextView = view.findViewById(R.id.view_settings_about_section_label)
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
            userAction.onUnlockAdsBlocker(activityContainer!!)
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
        themeSection.setCardBackgroundColor(sectionColor)
        aboutSection.setCardBackgroundColor(sectionColor)
    }

    override fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textPrimaryColorRes)
        adBlockerLabel.setTextColor(textColor)
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
        themeSectionLabel.setTextColor(textColor)
        aboutSectionLabel.setTextColor(textColor)
        adBlockerSubLabel.setTextColor(textColor)
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

    override fun setAdBlockerEnabled(enabled: Boolean) {
        adBlockerCheckBox.isChecked = enabled
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
            override fun onUnlockAdsBlocker(activityContainer: InAppManager.ActivityContainer) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val versionManager = ApplicationGraph.getVersionManager()
        val inAppManager = MonetizationGraph.getInAppManager()
        val adBlockerManager = ApplicationGraph.getAdBlockerManager()
        SettingsPresenter(
                this,
                themeManager,
                versionManager,
                inAppManager,
                adBlockerManager
        )
    }
}