package com.mercandalli.android.browser.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.browser.ad_blocker.AdBloackerModule
import com.mercandalli.android.browser.theme.ThemeModule
import com.mercandalli.android.browser.thread.MainThreadModule
import com.mercandalli.android.browser.toast.ToastModule
import com.mercandalli.android.browser.version.VersionModule

class ApplicationGraph(
        private val context: Context
) {

    private val adBlockerManagerInternal by lazy { AdBloackerModule().createAdBlockerManager(context) }
    private val mainThreadPostInternal by lazy { MainThreadModule().createMainThreadPost() }
    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }
    private val toastManagerInternal by lazy { ToastModule().createToastManager(context, mainThreadPostInternal) }
    private val versionManagerInternal by lazy { VersionModule().createVersionManager(context) }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }

        @JvmStatic
        fun getAdBlockerManager() = graph!!.adBlockerManagerInternal

        @JvmStatic
        fun getThemeManager() = graph!!.themeManagerInternal

        @JvmStatic
        fun getToastManager() = graph!!.toastManagerInternal

        @JvmStatic
        fun getVersionManager() = graph!!.versionManagerInternal
    }
}