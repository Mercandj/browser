package com.mercandalli.android.browser.main

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import android.webkit.WebView
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.mercandalli.android.browser.BuildConfig
import com.mercandalli.android.browser.ad_blocker.AdBlocker
import com.mercandalli.android.browser.remote_config.RemoteConfig
import com.mercandalli.android.browser.monetization.MonetizationGraph
import com.mercandalli.android.browser.monetization.MonetizationLog
import io.fabric.sdk.android.Fabric

/**
 * The [Application] of this project.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup fabric
        setupCrashlytics()

        setupMonetizationGraph()
        setupApplicationGraph()
        AdBlocker.init(this)

        // Debuggable WebView
        if (BuildConfig.DEBUG) {
            enableDebuggableWebView()
        }
    }

    private fun setupApplicationGraph() {
        ApplicationGraph.init(this)
    }

    private fun setupMonetizationGraph() {
        val monetizationLog = object : MonetizationLog {
            override fun d(tag: String, message: String) {
                Log.d(tag, message)
            }
        }
        val activityAction = object : MonetizationGraph.ActivityAction {
            override fun startFirstActivity() {
                MainActivity.start(this@MainApplication)
            }
        }
        MonetizationGraph.init(
                this,
                monetizationLog,
                activityAction
        )
        MonetizationGraph.getPurchaseManager().initialize()
    }

    private fun setupCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
        Fabric.with(this, crashlyticsKit)
    }

    private fun enableDebuggableWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
    }

    companion object {

        @JvmStatic
        fun onOnBoardingStarted() {
            val remoteConfig = ApplicationGraph.getRemoteConfig()
            updateOnBoardingStorePageAvailable(remoteConfig)
            val listener = object : RemoteConfig.Listener {
                override fun onRemoteConfigChanged() {
                    updateOnBoardingStorePageAvailable(remoteConfig)
                }
            }
            remoteConfig.registerListener(listener)
        }

        private fun updateOnBoardingStorePageAvailable(remoteConfig: RemoteConfig) {
            val available = remoteConfig.isOnBoardingStoreAvailable()
            MonetizationGraph.setOnBoardingStorePageAvailable(available)
        }
    }
}
