package com.mercandalli.android.browser.floating

import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.main.MainActivity
import com.mercandalli.android.browser.web.MainWebView

class FloatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FloatingContract.Screen {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_floating, this)
    private val mainWebView: MainWebView = view.findViewById(R.id.view_floating_main_web_view)
    private val statusBar: View = view.findViewById(R.id.view_floating_status_bar)
    private val statusBarTitle: TextView = view.findViewById(R.id.view_floating_status_bar_title)
    private val statusBarQuit: View = view.findViewById(R.id.view_floating_status_bar_quit)
    private val statusBarCollapse: View = view.findViewById(R.id.view_floating_status_bar_collapse)
    private val statusBarFullscreen: View = view.findViewById(R.id.view_floating_status_bar_fullscreen)
    private val statusBarHome: View = view.findViewById(R.id.view_floating_status_bar_home)
    private val windowManager by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val userAction = createUserAction()
    private val visibleDisplayFrame = Rect()

    private var listener: Listener? = null
    private var expandedWidth = 0
    private var expandedHeight = 0
    private var collapsedWidth = 0
    private var collapsedHeight = 0

    init {
        statusBarQuit.setOnClickListener { userAction.onQuitClicked() }
        statusBarFullscreen.setOnClickListener { userAction.onFullscreenClicked(mainWebView.url) }
        statusBarCollapse.setOnClickListener { userAction.onCollapseClicked() }
        statusBarHome.setOnClickListener { userAction.onHomeClicked() }
        setOnTouchListener(createOnTouchListener())

        if (!isInEditMode && viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener {
                getWindowVisibleDisplayFrame(visibleDisplayFrame)
            }
        }
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
        listener?.onRemovedFromWindowManager(this)
    }

    override fun expand() {
        val layoutParams = layoutParams as WindowManager.LayoutParams
        layoutParams.width = expandedWidth
        layoutParams.height = expandedHeight
        windowManager.updateViewLayout(this, layoutParams)
    }

    override fun collapse() {
        val layoutParams = layoutParams as WindowManager.LayoutParams
        layoutParams.width = collapsedWidth
        layoutParams.height = collapsedHeight
        windowManager.updateViewLayout(this, layoutParams)
    }

    override fun reload() {
        mainWebView.reload()
    }

    override fun showStatusBarTitle() {
        statusBarTitle.visibility = VISIBLE
    }

    override fun hideStatusBarTitle() {
        statusBarTitle.visibility = GONE
    }

    override fun setPrimaryTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        statusBarTitle.setTextColor(color)
    }

    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        statusBar.setBackgroundColor(color)
    }

    override fun navigateToMainActivity(url: String) {
        MainActivity.start(context, url)
    }

    override fun isCollapsed(): Boolean {
        val layoutParams = layoutParams as WindowManager.LayoutParams
        return layoutParams.width == collapsedWidth
    }

    override fun loadUrl(url: String) {
        mainWebView.load(url)
    }

    override fun showFullscreenButton() {
        statusBarFullscreen.visibility = VISIBLE
    }

    override fun hideFullscreenButton() {
        statusBarFullscreen.visibility = GONE
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setExpandedSize(width: Float, height: Float) {
        expandedWidth = width.toInt()
        expandedHeight = height.toInt()
    }

    fun setCollapsedSize(width: Float, height: Float) {
        collapsedWidth = width.toInt()
        collapsedHeight = height.toInt()
    }

    fun load(configuration: FloatingManager.Configuration) {
        userAction.onLoad(configuration)
    }

    fun close() {
        removeFromWindowManager()
    }

    private fun createOnTouchListener() = object : OnTouchListener {

        val lastDownEvent = PointF()
        val lastUpEvent = PointF()

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            val x = event.rawX
            val y = event.rawY
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastDownEvent.x = event.x
                    lastDownEvent.y = event.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val reMappedX = reMapX(x)
                    val reMappedY = reMapY(y)
                    updateViewLayout(reMappedX, reMappedY)
                    true
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    lastUpEvent.x = event.x
                    lastUpEvent.y = event.y
                    true
                }
                else -> false
            }
        }

        /**
         * Remap a raw x coordinate from a touch event [MotionEvent.ACTION_MOVE].
         *
         * @param rawX the raw x coordinate.
         * @return the remapped x.
         */
        private fun reMapX(rawX: Float) = rawX - lastDownEvent.x - x

        /**
         * Remap a raw y coordinate from a touch event [MotionEvent.ACTION_MOVE].
         *
         * @param rawY the raw y coordinate.
         * @return the remapped y.
         */
        private fun reMapY(rawY: Float) = rawY - lastDownEvent.y - visibleDisplayFrame.top.toFloat() - y
    }

    private fun updateViewLayout(x: Float, y: Float) {
        val params = layoutParams as WindowManager.LayoutParams
        params.x = x.toInt()
        params.y = y.toInt()
        windowManager.updateViewLayout(this, params)
    }

    private fun createUserAction(): FloatingContract.UserAction {
        if (isInEditMode) {
            return object : FloatingContract.UserAction {
                override fun onAttachedToWindow() {}
                override fun onDetachedFromWindow() {}
                override fun onQuitClicked() {}
                override fun onFullscreenClicked(url: String) {}
                override fun onCollapseClicked() {}
                override fun onHomeClicked() {}
                override fun onLoad(configuration: FloatingManager.Configuration) {}
            }
        }
        val themeManager = ApplicationGraph.getThemeManager()
        val searchEngineManager = ApplicationGraph.getSearchEngineManager()
        return FloatingPresenter(
            this,
            themeManager,
            searchEngineManager
        )
    }

    interface Listener {

        fun onRemovedFromWindowManager(floatingView: FloatingView)
    }
}
