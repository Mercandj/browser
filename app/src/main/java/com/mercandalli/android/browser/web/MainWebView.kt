package com.mercandalli.android.browser.web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebStorage
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.ConsoleMessage
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import android.webkit.WebResourceResponse
import com.mercandalli.android.browser.ad_blocker.AdBlocker
import androidx.annotation.AttrRes
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph

class MainWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    var browserWebViewListener: BrowserWebViewListener? = null
    private val attributes = extractAttributes(context, attrs, defStyleAttr)

    init {
        if (!isInEditMode) {
            isFocusableInTouchMode = true
            isHorizontalScrollBarEnabled = true
            isVerticalScrollBarEnabled = true
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                isScrollbarFadingEnabled = true
            }
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

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d("jm/debug", consoleMessage.message() + " @ " + consoleMessage.lineNumber())
                    return true
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    url?.let {
                        injectCSS(it)
                    }
                    if (browserWebViewListener != null) {
                        browserWebViewListener!!.onProgressChanged()
                    }
                }
            }
            webViewClient = object : WebViewClient() {

                private val loadedUrls = HashMap<String, Boolean>()

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

                @SuppressWarnings("deprecation")
                override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                    if (!ApplicationGraph.getAdBlockerManager().isEnabled()) {
                        return super.shouldInterceptRequest(view, url)
                    }
                    val ad: Boolean
                    if (!loadedUrls.containsKey(url)) {
                        ad = AdBlocker.isAd(url)
                        loadedUrls[url] = ad
                    } else {
                        ad = loadedUrls[url]!!
                    }
                    return if (ad) {
                        AdBlocker.createEmptyResource()
                    } else {
                        super.shouldInterceptRequest(view, url)
                    }
                }

                @SuppressWarnings("deprecation")
                override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                    if (!ApplicationGraph.getAdBlockerManager().isEnabled()) {
                        return super.shouldInterceptRequest(view, request)
                    }
                    val url: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        request.url.toString()
                    } else {
                        return AdBlocker.createEmptyResource()
                    }
                    val ad: Boolean
                    if (!loadedUrls.containsKey(url)) {
                        ad = AdBlocker.isAd(url)
                        loadedUrls[url] = ad
                    } else {
                        ad = loadedUrls[url]!!
                    }
                    return if (ad) {
                        AdBlocker.createEmptyResource()
                    } else {
                        super.shouldInterceptRequest(view, request)
                    }
                }
            }

            attributes.sourceUrl?.let {
                load(it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (browserWebViewListener != null) {
            browserWebViewListener!!.onPageTouched()
        }
        return super.onTouchEvent(event)
    }

    fun clearData() {
        clearHistory()
        clearCache(true)
        clearMatches()
        clearCookies(context)
        WebStorage.getInstance().deleteAllData()
    }

    fun load(url: String) {
        loadUrl(url)
    }

    interface BrowserWebViewListener {
        fun onProgressChanged()
        fun onPageFinished()
        fun onPageTouched()
    }

    private fun clearCookies(context: Context) {
        val cookieManager = CookieManager.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cookieManager.removeAllCookies(null)
            cookieManager.flush()
        } else {
            val cookieSyncManager = CookieSyncManager.createInstance(context)
            cookieSyncManager.startSync()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }

    private fun injectCSS(url: String) {
        val webCssManager = ApplicationGraph.getWebCssManager()
        val css = webCssManager.getCss(url) ?: return
        loadCss(css)
    }

    private fun loadCss(cssContent: String) {
        loadUrl("javascript:(function() {" +
            "var parent = document.getElementsByTagName('head').item(0);" +
            "var style = document.createElement('style');" +
            "style.type = 'text/css';" +
            "style.innerHTML = window.atob('" + cssContent + "');" +
            "parent.appendChild(style)" +
            "})()"
        )
    }

    private fun extractAttributes(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ): Attributes {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainWebView, defStyleAttr, 0)
        val sourceUrl = typedArray.getString(R.styleable.MainWebView_src_url)
        typedArray.recycle()
        return Attributes(
            sourceUrl
        )
    }

    private inner class Attributes internal constructor(
        internal val sourceUrl: String?
    )
}
