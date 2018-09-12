package com.mercandalli.android.browser.ad_blocker

import android.content.SharedPreferences
import com.mercandalli.android.browser.product.ProductManager

class AdBlockerManagerImpl(
        private val sharedPreferences: SharedPreferences,
        private val productManager: ProductManager
) : AdBlockerManager {

    private var enabled = false
    private var loaded = false

    override fun isFeatureAvailable() = productManager.isFullVersionAvailable()

    override fun isEnabled(): Boolean {
        if (!isFeatureAvailable()) {
            return false
        }
        load()
        return enabled
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        sharedPreferences.edit().putBoolean(KEY_ENABLED, this.enabled).apply()
    }

    private fun load() {
        if (loaded) {
            return
        }
        loaded = true
        enabled = sharedPreferences.getBoolean(KEY_ENABLED, enabled)
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "ad-blocker"
        private const val KEY_ENABLED = "enabled"
    }
}