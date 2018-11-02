package com.mercandalli.android.wear.browser.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.wear.browser.main_thread.MainThreadModule
import com.mercandalli.android.wear.browser.network.NetworkModule
import com.mercandalli.android.wear.browser.suggestion.SuggestionModule
import com.mercandalli.android.wear.browser.theme.ThemeModule

class ApplicationGraph(
    private val context: Context
) {

    private val mainThreadPostInternal by lazy { MainThreadModule().createMainThreadPost() }
    private val okHttpClientLazyInternal by lazy { NetworkModule(context).createOkHttpClientLazy() }
    private val networkManagerInternal by lazy { NetworkModule(context).createNetworkManager() }
    private val suggestionManagerInternal by lazy { SuggestionModule().createSuggestionManager() }
    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }

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
        fun getMainThreadPost() = graph!!.mainThreadPostInternal

        @JvmStatic
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazyInternal

        @JvmStatic
        fun getNetworkManager() = graph!!.networkManagerInternal

        @JvmStatic
        fun getSuggestionManager() = graph!!.suggestionManagerInternal

        @JvmStatic
        fun getThemeManager() = graph!!.themeManagerInternal
    }
}
