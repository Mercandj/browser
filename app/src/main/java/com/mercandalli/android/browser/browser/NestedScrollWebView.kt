package com.mercandalli.android.browser.browser

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.view.MotionEventCompat
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

open class NestedScrollWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr), NestedScrollingChild {

    private var lastMotionY = 0
    private var nestedYOffset = 0

    private val scrollOffset = IntArray(2)
    private val scrollConsumed = IntArray(2)

    private var childHelper: NestedScrollingChildHelper? = null

    init {
        if (!isInEditMode) {
            childHelper = NestedScrollingChildHelper(this)
            isNestedScrollingEnabled = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var result = false
        val trackedEvent = MotionEvent.obtain(event)
        val action = MotionEventCompat.getActionMasked(event)

        if (action == MotionEvent.ACTION_DOWN) {
            nestedYOffset = 0
        }

        val y = event.y.toInt()
        event.offsetLocation(0f, nestedYOffset.toFloat())

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                lastMotionY = y
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                result = super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaY = lastMotionY - y

                if (dispatchNestedPreScroll(0, deltaY, scrollConsumed, scrollOffset)) {
                    deltaY -= scrollConsumed[1]
                    trackedEvent.offsetLocation(0f, scrollOffset[1].toFloat())
                    nestedYOffset += scrollOffset[1]
                }

                lastMotionY = y - scrollOffset[1]

                val oldY = scrollY
                val newScrollY = Math.max(0, oldY + deltaY)
                val dyConsumed = newScrollY - oldY
                val dyUnconsumed = deltaY - dyConsumed

                if (dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, scrollOffset)) {
                    lastMotionY -= scrollOffset[1]
                    trackedEvent.offsetLocation(0f, scrollOffset[1].toFloat())
                    nestedYOffset += scrollOffset[1]
                }

                result = super.onTouchEvent(trackedEvent)
                trackedEvent.recycle()
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll()
                result = super.onTouchEvent(event)
            }
        }
        return result
    }

    // NestedScrollingChild

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        childHelper!!.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return childHelper!!.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return childHelper!!.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        childHelper!!.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return childHelper!!.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            offsetInWindow: IntArray?): Boolean {
        return childHelper!!.dispatchNestedScroll(
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(
            dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return childHelper!!.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(
            velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return childHelper!!.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return childHelper!!.dispatchNestedPreFling(velocityX, velocityY)
    }
}