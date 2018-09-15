package com.mercandalli.android.browser.suggestion

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class SuggestionTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val themeManager by lazy {
        ApplicationGraph.getThemeManager()
    }
    private val themeListener = createThemeListener()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetachedFromWindow() {
        themeManager.unregisterThemeListener(themeListener)
        super.onDetachedFromWindow()
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        val color = ContextCompat.getColor(context, theme.textPrimaryColorRes)
        setTextColor(color)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }
}