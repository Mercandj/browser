package com.mercandalli.android.browser.browser

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.webkit.*

class BrowserWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode) {
            isFocusableInTouchMode = true

            val settings = settings
            settings.mediaPlaybackRequiresUserGesture = false
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = false
            settings.displayZoomControls = false

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d("jm/debug", consoleMessage.message() + " @ " + consoleMessage.lineNumber())
                    return true
                }
            }
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView, url: String) {

                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    // The Error Page doesn't work inside API 17 et 18
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        return
                    }
                }
            }

            isHorizontalScrollBarEnabled = true
            isVerticalScrollBarEnabled = true
            isScrollContainer = true
            isClickable = true
        }
    }
}
