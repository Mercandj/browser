package com.mercandalli.android.browser.toast

import android.content.Context
import com.mercandalli.android.browser.main.ApplicationGraph

class ToastModule(
    private val context: Context
) {

    fun createToastManager(): ToastManager {
        val mainThreadPost = ApplicationGraph.getMainThreadPost()
        return ToastManagerImpl(
            context.applicationContext,
            mainThreadPost
        )
    }
}
