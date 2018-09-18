package com.mercandalli.android.browser.floating

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.main.MainActivity
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
    private val visibleDisplayFrame = Rect()

    init {
        statusBarQuit.setOnClickListener { userAction.onQuitClicked() }
        statusBarExpand.setOnClickListener { userAction.onExpandClicked(mainWebView.url) }
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

    override fun navigateToMainActivity(url: String) {
        MainActivity.start(context, url)
    }

    fun load(url: String) {
        mainWebView.load(url)
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
                override fun onExpandClicked(url: String) {}
            }
        }
        val themeManager = ApplicationGraph.getThemeManager()
        return FloatingPresenter(
                this,
                themeManager
        )
    }
}