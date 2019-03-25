package com.mercandalli.android.browser.web_css

interface WebCssManager {

    fun getCss(
        url: String
    ): String?
}
