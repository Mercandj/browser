package com.mercandalli.android.browser.ad_blocker

import java.net.MalformedURLException
import java.net.URL

internal object UrlUtils {

    @Throws(MalformedURLException::class)
    fun getHost(url: String): String {
        return URL(url).host
    }
}