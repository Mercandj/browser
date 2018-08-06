package com.mercandalli.android.browser.settings

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
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

    private val themeSection: CardView = view.findViewById(R.id.view_settings_theme_section)
    private val themeSectionLabel: TextView = view.findViewById(R.id.view_settings_theme_section_label)
    private val themeLabel: TextView = view.findViewById(R.id.view_settings_theme_label)
    private val themeSubLabel: TextView = view.findViewById(R.id.view_settings_theme_sublabel)
    private val themeCheckBox: CheckBox = view.findViewById(R.id.view_settings_theme)

    private val userAction = createUserAction()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        userAction.onDetached()
    }

    override fun setDarkThemeCheckBox(checked: Boolean) {

    }

    override fun setSectionColor(sectionColorRes: Int) {

    }

    override fun setTextPrimaryColorRes(textPrimaryColorRes: Int) {

    }

    override fun setTextSecondaryColorRes(textSecondaryColorRes: Int) {

    }

    private fun createUserAction(): SettingsContract.UserAction = if (isInEditMode) {
        object : SettingsContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        SettingsPresenter(
                this,
                themeManager
        )
    }

}