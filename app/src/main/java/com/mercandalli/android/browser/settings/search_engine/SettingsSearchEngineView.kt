@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.settings.search_engine

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.search_engine.SearchEngine
import com.mercandalli.android.browser.in_app.InAppManager

class SettingsSearchEngineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    SettingsSearchEngineContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings_search_engine, this)

    private val searchEngineRow: View = view.findViewById(R.id.view_settings_search_engine_row)
    private val searchEngineUnlockRow: View = view.findViewById(R.id.view_settings_search_engine_unlock_row)
    private val searchEngineSection: CardView = view.findViewById(R.id.view_settings_search_engine_section)
    private val searchEngineSectionLabel: TextView = view.findViewById(R.id.view_settings_search_engine_section_label)
    private val searchEngineLabel: TextView = view.findViewById(R.id.view_settings_search_engine_label)
    private val searchEngineSubLabel: TextView = view.findViewById(R.id.view_settings_search_engine_sublabel)
    private val searchEngineUnlockLabel: TextView = view.findViewById(R.id.view_settings_search_engine_unlock_label)
    private val searchEngineUnlockSubLabel: TextView = view.findViewById(R.id.view_settings_search_engine_unlock_sublabel)

    private val userAction = createUserAction()

    private var activityContainer: InAppManager.ActivityContainer? = null

    init {
        orientation = LinearLayout.VERTICAL
        searchEngineRow.setOnClickListener {
            userAction.onSearchEngineRowClicked()
        }
        searchEngineUnlockRow.setOnClickListener {
            userAction.onSearchEngineUnlockRowClicked(activityContainer!!)
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

    override fun setSectionColor(@ColorRes sectionColorRes: Int) {
        val sectionColor = ContextCompat.getColor(context, sectionColorRes)
        searchEngineSection.setCardBackgroundColor(sectionColor)
    }

    override fun setTextPrimaryColorRes(@ColorRes textPrimaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textPrimaryColorRes)
        searchEngineLabel.setTextColor(textColor)
        searchEngineUnlockLabel.setTextColor(textColor)
    }

    override fun setTextSecondaryColorRes(@ColorRes textSecondaryColorRes: Int) {
        val textColor = ContextCompat.getColor(context, textSecondaryColorRes)
        searchEngineSectionLabel.setTextColor(textColor)
        searchEngineSubLabel.setTextColor(textColor)
        searchEngineUnlockSubLabel.setTextColor(textColor)
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

    fun setActivityContainer(activityContainer: InAppManager.ActivityContainer) {
        this.activityContainer = activityContainer
    }

    private fun createUserAction(): SettingsSearchEngineContract.UserAction = if (isInEditMode) {
        object : SettingsSearchEngineContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onSearchEngineRowClicked() {}
            override fun onSearchEngineUnlockRowClicked(activityContainer: InAppManager.ActivityContainer) {}
        }
    } else {
        val themeManager = ApplicationGraph.getThemeManager()
        val productManager = ApplicationGraph.getProductManager()
        val searchEngineManager = ApplicationGraph.getSearchEngineManager()
        SettingsSearchEnginePresenter(
            this,
            themeManager,
            productManager,
            searchEngineManager
        )
    }
}
