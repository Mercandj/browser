package com.mercandalli.android.browser.toast

import android.content.Context
import com.mercandalli.android.browser.main_thread.MainThreadPost

class ToastModule {

    fun createToastManager(
            context: Context,
            mainThreadPost: MainThreadPost
    ): ToastManager {
        return ToastManagerImpl(
                context.applicationContext,
                mainThreadPost
        )
    }
}