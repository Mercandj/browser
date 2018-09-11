package com.mercandalli.android.browser.ad_blocker

interface AdBlockerManager {

    fun isEnabled(): Boolean

    fun setEnabled(enabled: Boolean)
}