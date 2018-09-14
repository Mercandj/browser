package com.mercandalli.android.browser.hash

import android.content.Context

class HashModule(
        private val context: Context
) {

    fun createHashManager(): HashManager {
        return HashManagerImpl()
    }
}