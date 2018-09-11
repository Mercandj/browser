package com.mercandalli.android.browser.ad_blocker

import android.content.Context

class AdBloackerModule {

    fun createAdBlockerManager(context: Context): AdBlockerManager {
        val sharedPreferences = context.getSharedPreferences(
                AdBlockerManagerImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        )
        return AdBlockerManagerImpl(
                sharedPreferences
        )
    }
}