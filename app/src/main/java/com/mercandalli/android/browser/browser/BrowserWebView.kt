package com.mercandalli.android.browser.browser

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import com.mercandalli.android.browser.main.Constants

class BrowserWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollWebView(context, attrs, defStyleAttr) {

    var browserWebViewListener: BrowserWebViewListener? = null
    private val WEB_VIEW_MIME_TYPE = "text/html"
    private val WEB_VIEW_ENCODING = "UTF-8"

    init {
        if (!isInEditMode) {
            isFocusableInTouchMode = true
            isHorizontalScrollBarEnabled = true
            isVerticalScrollBarEnabled = true
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
            isScrollbarFadingEnabled = true
            isScrollContainer = true
            isClickable = true

            val settings = settings
            settings.mediaPlaybackRequiresUserGesture = false
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.userAgentString = Constants.USER_AGENT

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d("jm/debug", consoleMessage.message() + " @ " + consoleMessage.lineNumber())
                    return true
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (browserWebViewListener != null) {
                        browserWebViewListener!!.onProgressChanged()
                    }
                }
            }
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView, url: String) {
                    if (browserWebViewListener != null) {
                        browserWebViewListener!!.onPageFinished()
                    }
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    // The Error Page doesn't work inside API 17 et 18
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        return
                    }
                }
            }
        }
    }

    fun load(url: String) {
        /*
        if(url.contains("youtube")) {
            loadDataWithBaseURL(
                    "http://www.google.com/",
                    BrowserUtils.getHtml(context),
                    WEB_VIEW_MIME_TYPE,
                    WEB_VIEW_ENCODING,
                    "")
            loadUrl(String.format("javascript:loadUrl(%s);", url))
        }
        */
        loadUrl(url)
    }

    interface BrowserWebViewListener {
        fun onProgressChanged()
        fun onPageFinished()
    }
}
