package com.mercandalli.android.browser.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.theme.ThemeModule
import com.mercandalli.android.browser.thread.MainThreadModule
import com.mercandalli.android.browser.thread.MainThreadPost

class ApplicationGraph(
        private val context: Context
) {

    private val mainThreadPostInternal by lazy { MainThreadModule().provideMainThreadPost() }
    private val themeManagerInternal by lazy { ThemeModule(context).provideThemeManager() }

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
        fun getMainThreadPost(): MainThreadPost {
            return graph!!.mainThreadPostInternal
        }

        @JvmStatic
        fun getThemeManager(): ThemeManager {
            return graph!!.themeManagerInternal
        }
    }
}