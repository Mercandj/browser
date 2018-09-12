package com.mercandalli.android.browser.ad_blocker

import android.content.Context
import com.mercandalli.android.browser.main.ApplicationGraph

class AdBlockerModule(
        private val context: Context
) {

    fun createAdBlockerManager(): AdBlockerManager {
        val sharedPreferences = context.getSharedPreferences(
                AdBlockerManagerImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        )
        val productManager = ApplicationGraph.getProductManager()
        return AdBlockerManagerImpl(
                sharedPreferences,
                productManager
        )
    }
}