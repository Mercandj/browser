package com.mercandalli.android.browser.settings.ad_blocker

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.in_app.InAppManager

class SettingsAdBlockerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
        SettingsAdBlockerContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_ad_blocker, this)

    private val adBlockerRow: View = view.findViewById(R.id.view_settings_ad_blocker_row)
    private val adBlockerUnlockRow: View = view.findViewById(R.id.view_settings_ad_blocker_unlock_row)
    private val adBlockerSection: CardView = view.findViewById(R.id.view_settings_ad_blocker_section)
    private val adBlockerSectionLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_section_label)
    private val adBlockerLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_label)
    private val adBlockerSubLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_sublabel)
    private val adBlockerCheckBox: CheckBox = view.findViewById(R.id.view_settings_ad_blocker)
    private val adBlockerUnlockLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_unlock_label)
    private val adBlockerUnlockSubLabel: TextView = view.findViewById(R.id.view_settings_ad_blocker_unlock_sublabel)

    private val userAction = createUserAction()

    private var activityContainer: InAppManager.ActivityContainer? = null

    init {
        orientation = LinearLayout.VERTICAL
        adBlockerRow.setOnClickListener {
            val isChecked = !adBlockerCheckBox.isChecked
            adBlockerCheckBox.isChecked = isChecked
            userAction.onAdBlockerCheckBoxCheckedChanged(isChecked)
        }
        adBlockerUnlockRow.setOnClickListener {
            userAction.onAdBlockerUnlockRowClicked(activityContainer!!)
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
        adBlockerSection.setCardBackgroundColor(sectionColor)
    }

    override fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textPrimaryColorRes)
        adBlockerLabel.setTextColor(textColor)
        adBlockerUnlockLabel.setTextColor(textColor)
        adBlockerCheckBox.setTextColor(textColor)
    }

    override fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textSecondaryColorRes)
        adBlockerSectionLabel.setTextColor(textColor)
        adBlockerSubLabel.setTextColor(textColor)
        adBlockerUnlockSubLabel.setTextColor(textColor)
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

    fun setActivityContainer(activityContainer: InAppManager.ActivityContainer) {
        this.activityContainer = activityContainer
    }

    private fun createUserAction(): SettingsAdBlockerContract.UserAction = if (isInEditMode) {
        object : SettingsAdBlockerContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onAdBlockerCheckBoxCheckedChanged(isChecked: Boolean) {}
            override fun onAdBlockerUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val adBlockerManager = ApplicationGraph.getAdBlockerManager()
        val productManager = ApplicationGraph.getProductManager()
        SettingsAdBlockerPresenter(
                this,
                themeManager,
                adBlockerManager,
                productManager
        )
    }
}