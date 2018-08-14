package com.mercandalli.android.browser.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.theme.ThemeModule
import com.mercandalli.android.browser.thread.MainThreadModule
import com.mercandalli.android.browser.thread.MainThreadPost
import com.mercandalli.android.browser.toast.ToastManager
import com.mercandalli.android.browser.toast.ToastModule
import com.mercandalli.android.browser.version.VersionManager
import com.mercandalli.android.browser.version.VersionModule

class ApplicationGraph(
        private val context: Context
) {

    private val mainThreadPostInternal by lazy { MainThreadModule().provideMainThreadPost() }
    private val themeManagerInternal by lazy { ThemeModule(context).provideThemeManager() }
    private val toastManagerInternal by lazy { ToastModule().provideToastManager(context, mainThreadPostInternal) }
    private val versionManagerInternal by lazy { VersionModule().provideVersionManager(context) }

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
        fun getThemeManager(): ThemeManager {
            return graph!!.themeManagerInternal
        }

        @JvmStatic
        fun getToastManager(): ToastManager {
            return graph!!.toastManagerInternal
        }

        @JvmStatic
        fun getVersionManager(): VersionManager {
            return graph!!.versionManagerInternal
        }
    }
}