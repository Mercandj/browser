@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.on_boarding

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class OnBoardingViewPager : ViewPager {

    private var startDragX = 0F
    private var swipeOutAtEndListener: SwipeOutAtEndListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (currentItem == adapter!!.count - 1) {
            val action = ev.action
            val x = ev.x
            when (action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> startDragX = x
                MotionEvent.ACTION_UP -> if (x < startDragX) {
                    swipeOutAtEndListener?.onSwipeOutAtEnd()
                } else {
                    startDragX = 0F
                }
            }
        } else {
            startDragX = 0F
        }
        return super.onTouchEvent(ev)
    }

    fun setSwipeOutAtEndListener(swipeOutAtEndListener: SwipeOutAtEndListener) {
        this.swipeOutAtEndListener = swipeOutAtEndListener
    }

    interface SwipeOutAtEndListener {

        fun onSwipeOutAtEnd()
    }
}
