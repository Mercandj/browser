package com.mercandalli.android.browser.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
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
import com.mercandalli.android.browser.browser.BrowserView
import com.mercandalli.android.browser.keyboard.KeyboardUtils
import com.mercandalli.android.browser.settings.SettingsActivity
import com.mwm.android.publishing.StoreActivity

class MainActivity : AppCompatActivity(), MainActivityContract.Screen {

    private val toolbar: Toolbar by bind(R.id.activity_main_toolbar)
    private val appBarLayout: AppBarLayout by bind(R.id.activity_main_app_bar_layout)
    private val webView: BrowserView by bind(R.id.activity_main_web_view)
    private val progress: ProgressBar by bind(R.id.activity_main_progress)
    private val input: EditText by bind(R.id.activity_main_search)
    private val more: View by bind(R.id.activity_main_more)

    private val browserWebViewListener = createBrowserWebViewListener()
    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        more.setOnClickListener { showOverflowPopupMenu(more) }
        webView.browserWebViewListener = browserWebViewListener
        input.setOnEditorActionListener(createOnEditorActionListener())

        if (savedInstanceState == null) {
            navigateHome()
        }

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapsed = -verticalOffset == appBarLayout.height
            userAction.onToolbarCollapsed(collapsed)
        }
        userAction.onCreate()
    }

    override fun onDestroy() {
        webView.browserWebViewListener = null
        userAction.onDestroy()
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
        SettingsActivity.start(this)
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

    override fun setWindowBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.setBackgroundDrawable(ColorDrawable(color))
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.statusBarColor = color
    }

    override fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        toolbar.setBackgroundColor(color)
    }

    override fun setInputTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        input.setTextColor(color)
    }

    override fun navigateToStore() {
        StoreActivity.start(this)
    }

    private fun createBrowserWebViewListener() = object : BrowserView.BrowserWebViewListener {
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

    private fun createUserAction(): MainActivityContract.UserAction {
        val themeManager = ApplicationGraph.getThemeManager()
        val toastManager = ApplicationGraph.getToastManager()
        return MainActivityPresenter(
                this,
                themeManager,
                toastManager
        )
    }

    private fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
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
