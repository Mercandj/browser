package com.mercandalli.android.browser.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.CheckBox
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.keyboard.KeyboardUtils
import com.mercandalli.android.browser.settings.SettingsActivity
import com.mercandalli.android.browser.suggestion.SuggestionAdapter
import com.mercandalli.android.browser.monetization.MonetizationGraph
import com.mercandalli.android.browser.web.MainWebView

class MainActivity : AppCompatActivity(), MainActivityContract.Screen {

    private val toolbar: View by bind(R.id.activity_main_toolbar)
    private val toolbarShadow: View by bind(R.id.activity_main_toolbar_shadow)
    private val mainWebView: MainWebView by bind(R.id.activity_main_web_view)
    private val progress: ProgressBar by bind(R.id.activity_main_progress)
    private val input: EditText by bind(R.id.activity_main_search)
    private val inputClear: ImageView by bind(R.id.activity_main_clear_input)
    private val more: ImageView by bind(R.id.activity_main_more)
    private val suggestions: RecyclerView by bind(R.id.activity_main_recycler_view)
    private val suggestionsShadow: View by bind(R.id.activity_main_recycler_view_shadow)
    private val suggestionsAdapter = createSuggestionAdapter()
    private val fabClear: FloatingActionButton by bind(R.id.activity_main_fab_clear)

    private val emptyView: View by bind(R.id.activity_main_empty_view)
    private val emptyTextView: TextView by bind(R.id.activity_main_empty_view_text)
    private val emptyViewVideoCheckBox: CheckBox by bind(R.id.activity_main_video_check_box)
    private val emptyViewFloatingCheckBox: CheckBox by bind(R.id.activity_main_floating_check_box)
    private val emptyQuitTextView: TextView by bind(R.id.activity_main_quit)
    private val emptyBottomBar: View by bind(R.id.activity_main_bottom_bar)

    private val browserWebViewListener = createBrowserWebViewListener()
    private val userAction = createUserAction()
    private var forceDestroy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (MonetizationGraph.startOnBoardingIfNeeded(this)) {
            forceDestroy = true
            finish()
            MainApplication.onOnBoardingStarted()
            return
        }
        setContentView(R.layout.activity_main)
        more.setOnClickListener { showOverflowPopupMenu(more) }
        mainWebView.browserWebViewListener = browserWebViewListener
        mainWebView.setBackgroundColor(Color.TRANSPARENT)
        input.setOnEditorActionListener(createOnEditorActionListener())
        input.addTextChangedListener(createTextWatcher())
        emptyViewVideoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            userAction.onVideoCheckedChanged(isChecked)
        }
        emptyQuitTextView.setOnClickListener { userAction.onQuitClicked() }
        fabClear.setOnClickListener { userAction.onFabClearClicked() }
        inputClear.setOnClickListener { userAction.onInputClearClicked() }
        suggestions.layoutManager = LinearLayoutManager(this)
        suggestions.adapter = suggestionsAdapter
        userAction.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (forceDestroy) {
            return
        }
        mainWebView.browserWebViewListener = null
        userAction.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val url = intent?.extras?.getString(EXTRA_URL)
        userAction.onNewIntent(url)
    }

    override fun onResume() {
        super.onResume()
        userAction.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (forceDestroy) {
            return
        }
        userAction.onSaveInstanceState(outState)
        mainWebView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userAction.onRestoreInstanceState(savedInstanceState)
        mainWebView.restoreState(savedInstanceState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            userAction.onBackPressed(emptyView.visibility == VISIBLE)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun showUrl(url: String) {
        mainWebView.load(url)
    }

    override fun reload() {
        mainWebView.reload()
    }

    override fun webViewCanGoBack() = mainWebView.canGoBack()

    override fun webViewBack() {
        mainWebView.goBack()
    }

    override fun quit() {
        finish()
    }

    override fun navigateSettings() {
        SettingsActivity.start(this)
    }

    override fun clearData() {
        mainWebView.clearData()
    }

    override fun showClearDataMessage() {
        showSnackbar(R.string.activity_main_data_clear, Snackbar.LENGTH_SHORT)
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

    override fun showKeyboard() {
        input.postDelayed({
            input.isFocusableInTouchMode = true
            input.requestFocus()
            KeyboardUtils.showSoftInput(input)
        }, 200)
    }

    override fun hideKeyboard() {
        KeyboardUtils.hideSoftInput(input)
    }

    override fun resetSearchInput() {
        input.setText("")
    }

    override fun setWindowBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.setBackgroundDrawable(ColorDrawable(color))
        suggestions.setBackgroundColor(color)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.statusBarColor = color
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun setStatusBarDark(statusBarDark: Boolean) {
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (statusBarDark)
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        else
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        toolbar.setBackgroundColor(color)
    }

    override fun setPrimaryTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        input.setTextColor(color)
        emptyTextView.setTextColor(color)
        emptyViewVideoCheckBox.setTextColor(color)
        emptyViewFloatingCheckBox.setTextColor(color)
    }

    override fun setSecondaryTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        more.setColorFilter(color)
        inputClear.setColorFilter(color)
    }

    override fun setAccentTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        emptyQuitTextView.setTextColor(color)
    }

    override fun showFab() {
        if (!fabClear.isShown) {
            fabClear.show()
        }
    }

    override fun hideFab() {
        fabClear.hide()
    }

    override fun showWebView() {
        mainWebView.visibility = VISIBLE
    }

    override fun hideWebView() {
        mainWebView.visibility = GONE
    }

    override fun showEmptyView() {
        emptyView.visibility = VISIBLE
        emptyBottomBar.visibility = VISIBLE
    }

    override fun hideEmptyView() {
        emptyView.visibility = GONE
        emptyBottomBar.visibility = GONE
    }

    override fun showToolbar() {
        toolbar.visibility = VISIBLE
        toolbarShadow.visibility = VISIBLE
    }

    override fun hideToolbar() {
        toolbar.visibility = GONE
        toolbarShadow.visibility = GONE
    }

    override fun showSuggestions(suggestions: List<String>) {
        this.suggestions.visibility = VISIBLE
        suggestionsShadow.visibility = VISIBLE
        suggestionsAdapter.populate(suggestions)
    }

    override fun hideSuggestions() {
        suggestions.visibility = GONE
        suggestionsShadow.visibility = GONE
    }

    override fun showClearInput() {
        inputClear.visibility = VISIBLE
    }

    override fun hideClearInput() {
        inputClear.visibility = GONE
    }

    override fun isFloatingWindowChecked() = emptyViewFloatingCheckBox.isChecked

    override fun setInput(inputString: String) {
        input.setText(inputString)
        input.setSelection(input.text.length)
    }

    override fun showFloatingWindowCheckbox() {
        emptyViewFloatingCheckBox.visibility = VISIBLE
    }

    override fun hideFloatingWindowCheckbox() {
        emptyViewFloatingCheckBox.visibility = GONE
    }

    private fun showSnackbar(@StringRes text: Int, duration: Int) {
        Snackbar.make(findViewById(R.id.activity_main_bottom_bar), text, duration).show()
    }

    private fun createBrowserWebViewListener() = object : MainWebView.BrowserWebViewListener {
        override fun onPageFinished() {
            userAction.onPageLoadProgressChanged(100)
        }

        override fun onProgressChanged() {
            userAction.onPageLoadProgressChanged(mainWebView.progress)
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

    private fun createTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userAction.onSearchInputChanged(input.text.toString())
        }
    }

    private fun createSuggestionAdapter() = SuggestionAdapter(object : SuggestionAdapter.SuggestionClickListener {
        override fun onSuggestionClicked(suggestion: String) {
            userAction.onSuggestionClicked(suggestion)
        }

        override fun onSuggestionImageClicked(suggestion: String) {
            userAction.onSuggestionImageClicked(suggestion)
        }
    })

    private fun createUserAction(): MainActivityContract.UserAction {
        val themeManager = ApplicationGraph.getThemeManager()
        val searchEngineManager = ApplicationGraph.getSearchEngineManager()
        val suggestionManager = ApplicationGraph.getSuggestionManager()
        val floatingManager = ApplicationGraph.getFloatingManager()
        val productManager = ApplicationGraph.getProductManager()
        return MainActivityPresenter(
            this,
            themeManager,
            searchEngineManager,
            suggestionManager,
            floatingManager,
            productManager
        )
    }

    private fun <T : View> bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }

    companion object {
        private const val EXTRA_URL = "EXTRA_URL"

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }

        @JvmStatic
        fun start(context: Context, url: String) {
            val intent = Intent(context, MainActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }
}
