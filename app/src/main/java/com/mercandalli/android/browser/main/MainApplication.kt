package com.mercandalli.android.browser.main

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.webkit.WebView
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.mercandalli.android.browser.BuildConfig
import com.mercandalli.android.libs.monetization.Monetization
import com.mercandalli.android.libs.monetization.MonetizationGraph
import com.mercandalli.android.libs.monetization.log.MonetizationLog
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

            }
        }
        MonetizationGraph.init(
                this,
                Monetization.create(
                        "",
                        ""
                ),
                monetizationLog
        )
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
}
