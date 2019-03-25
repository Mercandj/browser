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
import com.mercandalli.android.browser.web_css.WebCssModule

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
    private val remoteConfigInternal by lazy { RemoteConfigModule().createRemoteConfig() }
    private val searchEngineManagerInternal by lazy { SearchEngineModule().createSearchEngineManager() }
    private val suggestionManagerInternal by lazy { SuggestionModule().createSuggestionManager() }
    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }
    private val toastManagerInternal by lazy { ToastModule(context).createToastManager() }
    private val updateManagerInternal by lazy { UpdateModule(context).createUpdateManager() }
    private val versionManagerInternal by lazy { VersionModule(context).createVersionManager() }
    private val webCssManagerInternal by lazy { WebCssModule(context).createWebCssManager() }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        fun getAdBlockerManager() = graph!!.adBlockerManagerInternal
        fun getAnalyticsManager() = graph!!.analyticsManagerInternal
        fun getDialogManager() = graph!!.dialogManagerInternal
        fun getFloatingManager() = graph!!.floatingManagerInternal
        fun getHashManager() = graph!!.hashManagerInternal
        fun getMainThreadPost() = graph!!.mainThreadPostInternal
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazyInternal
        fun getNetworkManager() = graph!!.networkManagerInternal
        fun getProductManager() = graph!!.productManagerInternal
        fun getRemoteConfig() = graph!!.remoteConfigInternal
        fun getSearchEngineManager() = graph!!.searchEngineManagerInternal
        fun getSuggestionManager() = graph!!.suggestionManagerInternal
        fun getThemeManager() = graph!!.themeManagerInternal
        fun getToastManager() = graph!!.toastManagerInternal
        fun getUpdateManager() = graph!!.updateManagerInternal
        fun getVersionManager() = graph!!.versionManagerInternal
        fun getWebCssManager() = graph!!.webCssManagerInternal

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }
    }
}
