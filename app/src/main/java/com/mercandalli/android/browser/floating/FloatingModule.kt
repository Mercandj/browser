package com.mercandalli.android.browser.floating

import android.content.Context

class FloatingModule(
        private val context: Context
) {

    fun createFloatingManager(): FloatingManager {
        return FloatingManagerImpl(context.applicationContext)
    }
}