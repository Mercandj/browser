package com.mercandalli.android.browser.locale

import com.mercandalli.android.browser.context.ContextUtils

object LocaleUtils {

    fun getLocaleCode(): String {
        val locales = ContextUtils.context.resources.configuration.locales
        val locale = locales[0]
        return locale.language
    }
}
