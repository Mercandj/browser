package com.mercandalli.android.browser.floating

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.main.MainWebView

class FloatingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        FloatingContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_floating, this)
    private val mainWebView: MainWebView = view.findViewById(R.id.view_floating_main_web_view)
    private val statusBar: View = view.findViewById(R.id.view_floating_status_bar)
    private val statusBarTitle: TextView = view.findViewById(R.id.view_floating_status_bar_title)
    private val statusBarQuit: View = view.findViewById(R.id.view_floating_status_bar_quit)
    private val statusBarMinify: View = view.findViewById(R.id.view_floating_status_bar_minify)
    private val statusBarExpand: View = view.findViewById(R.id.view_floating_status_bar_expand)
    private val windowManager by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val userAction = createUserAction()

    init {
        statusBarQuit.setOnClickListener { userAction.onQuitClicked() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetachedFromWindow()
        super.onDetachedFromWindow()
    }

    override fun removeFromWindowManager() {
        windowManager.removeView(this)
    }

    override fun reload() {
        mainWebView.reload()
    }

    override fun setPrimaryTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        statusBarTitle.setTextColor(color)
    }

    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        statusBar.setBackgroundColor(color)
    }

    fun load(url: String) {
        mainWebView.load(url)
    }

    private fun createUserAction(): FloatingContract.UserAction {
        if (isInEditMode) {
            return object : FloatingContract.UserAction {
                override fun onAttachedToWindow() {}
                override fun onDetachedFromWindow() {}
                override fun onQuitClicked() {}
            }
        }
        val themeManager = ApplicationGraph.getThemeManager()
        return FloatingPresenter(
                this,
                themeManager
        )
    }
}