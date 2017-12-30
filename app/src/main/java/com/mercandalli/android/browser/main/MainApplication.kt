package com.mercandalli.android.browser.main

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.webkit.WebView
import com.mercandalli.android.browser.BuildConfig


/**
 * The [Application] of this project.
 */
class MainApplication : Application() {

    private var mainComponent: MainComponent? = null

    override fun onCreate() {
        super.onCreate()

        application = this

        // Dagger
        setupGraph()

        // Debuggable WebView
        if (BuildConfig.DEBUG) {
            enableDebuggableWebView()
        }
    }

    /**
     * Set up the application dagger graph. See [MainComponent].
     */
    private fun setupGraph() {
        mainComponent = DaggerMainComponent.builder()
                .mainModule(MainModule(this))
                .build()
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
        private var application: MainApplication? = null

        /**
         * Retrieves the [MainComponent].
         *
         * @param context : The [Context] from which we want to retrieve the Dagger component.
         * @return : The [MainComponent].
         */
        @JvmStatic
        fun getAppComponent(): MainComponent {
            return application!!.mainComponent ?: throw IllegalStateException("Dagger graph not ready.")
        }
    }
}
