package com.mercandalli.android.browser.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.browser.BrowserWebView
import com.mercandalli.android.browser.keyboard.KeyboardUtils
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class MainActivity : AppCompatActivity(), MainActivityContract.Screen {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var webView: BrowserWebView
    private lateinit var progress: ProgressBar
    private lateinit var input: EditText
    private lateinit var more: View
    private val browserWebViewListener = createBrowserWebViewListener()
    private val themeManager = MainApplication.getAppComponent().provideThemeManager()
    private val themeListener = createThemeListener()
    private var component: MainActivityComponent? = null
    private lateinit var userAction: MainActivityContract.UserAction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        appBarLayout = findViewById(R.id.activity_main_app_bar_layout)
        webView = findViewById(R.id.activity_main_web_view)
        webView.browserWebViewListener = browserWebViewListener
        progress = findViewById(R.id.activity_main_progress)
        more = findViewById(R.id.activity_main_more)
        more.setOnClickListener { showOverflowPopupMenu(more) }
        input = findViewById(R.id.activity_main_search)
        input.setOnEditorActionListener(createOnEditorActionListener())

        themeManager.registerThemeListener(themeListener)
        updateTheme()

        if (savedInstanceState == null) {
            navigateHome()
        }

        component = DaggerMainActivityComponent.builder()
                .mainActivityModule(MainActivityModule(this))
                .mainComponent(MainApplication.getAppComponent())
                .build()
        userAction = component!!.provideMainActivityUserAction()

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapsed = -verticalOffset == appBarLayout.height
            userAction.onToolbarCollapsed(collapsed)
        }
    }

    override fun onDestroy() {
        webView.browserWebViewListener = null
        themeManager.unregisterThemeListener(themeListener)
        component = null
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            userAction.onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun setToolbarContentVisible(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        input.visibility = visibility
        more.visibility = visibility
    }

    //region MainActivityContract.Screen
    override fun showUrl(url: String) {
        webView.load(url)
    }

    override fun back() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

    override fun navigateHome() {
        webView.load("https://www.google.com/")
    }

    override fun navigateSettings() {
        Toast.makeText(this, "Not yet", Toast.LENGTH_SHORT).show()
    }

    override fun clearData() {
        webView.clearData()
    }

    override fun showClearDataMessage() {
        Toast.makeText(this, "Data cleared", Toast.LENGTH_SHORT).show()
    }

    override fun showLoader(progressPercent: Int) {
        progress.visibility = VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progress.setProgress(progressPercent, true)
        } else {
            progress.progress = progressPercent
        }
    }

    override fun hideLoader() {
        progress.visibility = GONE
    }

    override fun hideKeyboard() {
        KeyboardUtils.hideSoftInput(input)
    }

    override fun collapseToolbar() {
        appBarLayout.setExpanded(false)
    }

    override fun resetSearchInput() {
        input.setText("")
    }
    //endregion MainActivityContract.Screen

    private fun createBrowserWebViewListener() = object : BrowserWebView.BrowserWebViewListener {
        override fun onPageFinished() {
            userAction.onPageLoadProgressChanged(100)
        }

        override fun onProgressChanged() {
            userAction.onPageLoadProgressChanged(webView.progress)
        }

        override fun onPageTouched() {
            userAction.onPageTouched()
        }
    }

    private fun createOnEditorActionListener() = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER) {
            userAction.onSearchPerformed(v!!.text.toString())
            return@OnEditorActionListener true
        }
        false
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun showOverflowPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.menu_browser, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(createOnMenuItemClickListener())
        popupMenu.show()
    }

    private fun createOnMenuItemClickListener() = PopupMenu.OnMenuItemClickListener { item ->
        when (item!!.itemId) {
            R.id.menu_browser_home -> userAction.onHomeClicked()
            R.id.menu_browser_clear_data -> userAction.onClearDataClicked()
            R.id.menu_browser_settings -> userAction.onSettingsClicked()
        }
        false
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

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}
