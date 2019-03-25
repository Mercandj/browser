package com.mercandalli.android.browser.web_css

import android.content.Context
import com.mercandalli.android.browser.main.ApplicationGraph

class WebCssModule(
    private val context: Context
) {

    fun createWebCssManager(): WebCssManager {
        val themeManager = ApplicationGraph.getThemeManager()
        val addOn = createWebCssManagerAddOn()
        return WebCssManagerImpl(
            themeManager,
            addOn
        )
    }

    private fun createWebCssManagerAddOn() = object : WebCssManagerImpl.AddOn {
        override fun openAssets(fileName: String) = context.assets.open(fileName)
    }
}
