@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.settings_about

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph

class SettingsAboutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsAboutContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_about, this)

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

    init {
        orientation = LinearLayout.VERTICAL
        versionNameRow.setOnClickListener {
            userAction.onVersionNameRowClicked()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun setSectionColor(@ColorRes sectionColorRes: Int) {
        val sectionColor = ContextCompat.getColor(context, sectionColorRes)
        aboutSection.setCardBackgroundColor(sectionColor)
    }

    override fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textPrimaryColorRes)
        versionNameLabel.setTextColor(textColor)
        versionCodeLabel.setTextColor(textColor)
        longVersionCodeLabel.setTextColor(textColor)
    }

    override fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textSecondaryColorRes)
        aboutSectionLabel.setTextColor(textColor)
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

    override fun showSnackbar(@StringRes messageStringRes: Int, duration: Int) {
        val activity = context as Activity
        val view = activity.window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(view, messageStringRes, duration).show()
    }

    override fun showSnackbar(text: String, duration: Int) {
        val activity = context as Activity
        Snackbar.make(activity.window.decorView.findViewById(android.R.id.content), text, duration).show()
    }

    private fun createUserAction(): SettingsAboutContract.UserAction = if (isInEditMode) {
        object : SettingsAboutContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onVersionNameRowClicked() {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val versionManager = ApplicationGraph.getVersionManager()
        val productManager = ApplicationGraph.getProductManager()
        val dialogManager = ApplicationGraph.getDialogManager()
        val hashManager = ApplicationGraph.getHashManager()
        val addOn = object : SettingsAboutPresenter.AddOn {
            override fun getCurrentTimeMillis() = System.currentTimeMillis()
        }
        SettingsAboutPresenter(
            this,
            themeManager,
            versionManager,
            productManager,
            dialogManager,
            hashManager,
            addOn
        )
    }
}
