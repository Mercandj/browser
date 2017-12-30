package com.mercandalli.android.browser.browser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.keyboard.KeyboardUtils
import com.mercandalli.android.browser.main.Constants
import com.mercandalli.android.browser.main.MainApplication
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class BrowserActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, BrowserActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }

    private var appBarLayout: AppBarLayout? = null
    private var webView: BrowserWebView? = null
    private var progress: ProgressBar? = null
    private var input: EditText? = null
    private var home: View? = null
    private val browserWebViewListener = createBrowserWebViewListener()
    private val themeManager = MainApplication.getAppComponent().provideThemeManager()
    private val themeListener = createThemeListener()
    private val onOffsetChangedListener = createOnOffsetChangedListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        appBarLayout = findViewById(R.id.activity_main_app_bar_layout)
        webView = findViewById(R.id.activity_main_web_view)
        webView!!.browserWebViewListener = browserWebViewListener
        progress = findViewById(R.id.activity_main_progress)
        home = findViewById(R.id.activity_main_home)
        home!!.setOnClickListener { loadHomePage() }
        input = findViewById(R.id.activity_main_search)
        input!!.setOnEditorActionListener(createOnEditorActionListener())

        themeManager.registerThemeListener(themeListener)
        updateTheme()

        appBarLayout!!.addOnOffsetChangedListener(onOffsetChangedListener)

        if (savedInstanceState == null) {
            loadHomePage()
        }
    }

    override fun onDestroy() {
        webView!!.browserWebViewListener = null
        appBarLayout!!.removeOnOffsetChangedListener(onOffsetChangedListener)
        themeManager.unregisterThemeListener(themeListener)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        webView!!.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView!!.restoreState(savedInstanceState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView!!.canGoBack()) {
                        webView!!.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun loadHomePage() {
        webView!!.load(Constants.HOME_PAGE)
    }

    private fun createBrowserWebViewListener(): BrowserWebView.BrowserWebViewListener {
        return object : BrowserWebView.BrowserWebViewListener {
            override fun onPageFinished() {
                progress!!.visibility = GONE
            }

            override fun onProgressChanged() {
                val progressPercent = webView!!.progress
                if (progressPercent >= 100) {
                    progress!!.visibility = GONE
                    return
                }
                progress!!.visibility = VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress!!.setProgress(progressPercent, true)
                } else {
                    progress!!.progress = progressPercent
                }
            }
        }
    }

    private fun createOnEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { v, _, _ ->
            webView!!.load("https://www.google.fr/search?q=" + v!!.text.toString().replace(" ", "+"))
            appBarLayout!!.setExpanded(false)
            KeyboardUtils.hideSoftInput(input!!)
            true
        }
    }

    private fun createOnOffsetChangedListener(): AppBarLayout.OnOffsetChangedListener? {
        return AppBarLayout.OnOffsetChangedListener { _, _ -> }
    }

    private fun createThemeListener(): ThemeManager.ThemeListener {
        return object : ThemeManager.ThemeListener {
            override fun onThemeChanged() {
                updateTheme()
            }
        }
    }

    private fun updateTheme() {
        updateTheme(themeManager.theme)
    }

    private fun updateTheme(theme: Theme) {
        window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(
                this,
                theme.windowBackgroundColorRes)))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(
                    this,
                    theme.statusBarBackgroundColorRes)
        }
    }
}
