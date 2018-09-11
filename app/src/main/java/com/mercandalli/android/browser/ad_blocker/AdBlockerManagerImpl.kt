package com.mercandalli.android.browser.ad_blocker

import android.content.SharedPreferences

class AdBlockerManagerImpl(
        private val sharedPreferences: SharedPreferences
) : AdBlockerManager {

    private var enabled = false
    private var enabledLoaded = false

    override fun isEnabled(): Boolean {
        load()
        return enabled
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        sharedPreferences.edit().putBoolean(KEY, enabled).apply()
    }

    private fun load() {
        if (enabledLoaded) {
            return
        }
        enabledLoaded = true
        enabled = sharedPreferences.getBoolean(KEY, enabled)
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "AdBlockerManager"
        private const val KEY = "ad-blocker-enabled"
    }
}