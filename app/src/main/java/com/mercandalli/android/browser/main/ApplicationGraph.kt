package com.mercandalli.android.browser.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.browser.ad_blocker.AdBlockerModule
import com.mercandalli.android.browser.analytics.AnalyticsModule
import com.mercandalli.android.browser.dialog.DialogModule
import com.mercandalli.android.browser.floating.FloatingModule
import com.mercandalli.android.browser.hash.HashModule
import com.mercandalli.android.browser.network.NetworkModule
import com.mercandalli.android.browser.product.ProductModule
import com.mercandalli.android.browser.remote_config.RemoteConfigModule
import com.mercandalli.android.browser.search_engine.SearchEngineModule
import com.mercandalli.android.browser.suggestion.SuggestionModule
import com.mercandalli.android.browser.theme.ThemeModule
import com.mercandalli.android.browser.main_thread.MainThreadModule
import com.mercandalli.android.browser.toast.ToastModule
import com.mercandalli.android.browser.update.UpdateModule
import com.mercandalli.android.browser.version.VersionModule

class ApplicationGraph(
        private val context: Context
) {

    private val adBlockerManagerInternal by lazy { AdBlockerModule(context).createAdBlockerManager() }
    private val analyticsManagerInternal by lazy { AnalyticsModule(context).createAnalyticsManager() }
    private val dialogManagerInternal by lazy { DialogModule(context).createDialogManager() }
    private val floatingManagerInternal by lazy { FloatingModule(context).createFloatingManager() }
    private val hashManagerInternal by lazy { HashModule(context).createHashManager() }
    private val mainThreadPostInternal by lazy { MainThreadModule().createMainThreadPost() }
    private val okHttpClientLazyInternal by lazy { NetworkModule(context).createOkHttpClientLazy() }
    private val networkManagerInternal by lazy { NetworkModule(context).createNetworkManager() }
    private val productManagerInternal by lazy { ProductModule(context).createProductManager() }
    private val remoteConfigInternal by lazy { RemoteConfigModule().createRemoteConfig(mainThreadPostInternal) }
    private val searchEngineManagerInternal by lazy { SearchEngineModule().createSearchEngineManager() }
    private val suggestionManagerInternal by lazy { SuggestionModule().createSuggestionManager() }
    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }
    private val toastManagerInternal by lazy { ToastModule().createToastManager(context, mainThreadPostInternal) }
    private val updateManagerInternal by lazy { UpdateModule().createUpdateManager(context, versionManagerInternal) }
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
        fun getAnalyticsManager() = graph!!.analyticsManagerInternal

        @JvmStatic
        fun getDialogManager() = graph!!.dialogManagerInternal

        @JvmStatic
        fun getFloatingManager() = graph!!.floatingManagerInternal

        @JvmStatic
        fun getHashManager() = graph!!.hashManagerInternal

        @JvmStatic
        fun getMainThreadPost() = graph!!.mainThreadPostInternal

        @JvmStatic
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazyInternal

        @JvmStatic
        fun getNetworkManager() = graph!!.networkManagerInternal

        @JvmStatic
        fun getProductManager() = graph!!.productManagerInternal

        @JvmStatic
        fun getRemoteConfig() = graph!!.remoteConfigInternal

        @JvmStatic
        fun getSearchEngineManager() = graph!!.searchEngineManagerInternal

        @JvmStatic
        fun getSuggestionManager() = graph!!.suggestionManagerInternal

        @JvmStatic
        fun getThemeManager() = graph!!.themeManagerInternal

        @JvmStatic
        fun getToastManager() = graph!!.toastManagerInternal

        @JvmStatic
        fun getUpdateManager() = graph!!.updateManagerInternal

        @JvmStatic
        fun getVersionManager() = graph!!.versionManagerInternal
    }
}