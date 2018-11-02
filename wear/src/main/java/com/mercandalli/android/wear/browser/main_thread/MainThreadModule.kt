@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.wear.browser.main_thread

import android.os.Handler
import android.os.Looper

class MainThreadModule {

    fun createMainThreadPost(): MainThreadPost {
        val mainLooper = Looper.getMainLooper()
        return MainThreadPostImpl(
            mainLooper.thread,
            Handler(mainLooper)
        )
    }
}
