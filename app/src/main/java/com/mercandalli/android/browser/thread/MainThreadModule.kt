package com.mercandalli.android.browser.thread

import android.os.Handler
import android.os.Looper

class MainThreadModule {

    fun provideMainThreadPost(): MainThreadPost {
        val mainLooper = Looper.getMainLooper()
        return MainThreadPostImpl(
                mainLooper.thread,
                Handler(mainLooper)
        )
    }
}
