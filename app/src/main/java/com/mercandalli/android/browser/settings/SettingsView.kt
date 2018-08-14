package com.mercandalli.android.browser.settings

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph

class SettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr),
        SettingsContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings, this)

    private val themeRow: View = view.findViewById(R.id.view_settings_theme_row)
    private val themeSection: CardView = view.findViewById(R.id.view_settings_theme_section)
    private val themeSectionLabel: TextView = view.findViewById(R.id.view_settings_theme_section_label)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_theme_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_theme_sublabel)
    private val themeCheckBox: CheckBox = view.findViewById(R.id.view_settings_theme)

    private val versionName: TextView = view.findViewById(R.id.view_settings_app_version_name)
    private val versionCode: TextView = view.findViewById(R.id.view_settings_app_version_code)
    private val longVersionCode: TextView = view.findViewById(R.id.view_settings_app_long_version_code)

    private val userAction = createUserAction()

    init {
        themeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            userAction.onDarkThemeCheckBoxCheckedChanged(isChecked)
        }
        themeRow.setOnClickListener {
            val isChecked = !themeCheckBox.isChecked
            themeCheckBox.isChecked = isChecked
            userAction.onDarkThemeCheckBoxCheckedChanged(isChecked)
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
        themeSection.setCardBackgroundColor(sectionColor)
    }

    override fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textPrimaryColorRes)
        themeLabel.setTextColor(textColor)
        themeCheckBox.setTextColor(textColor)
    }

    override fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textSecondaryColorRes)
        themeSectionLabel.setTextColor(textColor)
        themeSubLabel.setTextColor(textColor)
    }

    override fun setVersionName(versionName: String) {
        this.versionName.text = versionName
    }

    override fun setVersionCode(versionCode: Int) {
        this.versionCode.text = versionCode.toString()
    }

    override fun setLongVersionCode(longVersionCode: Long) {
        this.longVersionCode.text = longVersionCode.toString()
    }

    private fun createUserAction(): SettingsContract.UserAction = if (isInEditMode) {
        object : SettingsContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val versionManager = ApplicationGraph.getVersionManager()
        SettingsPresenter(
                this,
                themeManager,
                versionManager
        )
    }
}