package com.mercandalli.android.browser.floating

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.view.ViewUtils

class FloatingManagerImpl(
        private val context: Context,
        private val floatingPermission: FloatingPermission,
        private val windowManager: WindowManager
) : FloatingManager {

    private val floatingViews = ArrayList<FloatingView>()

    override fun start(url: String) {
        if (!floatingPermission.canDrawOverOtherApps()) {
            floatingPermission.launchDrawOverOtherAppPermissionManager()
            return
        }
        val width = getScreenMinWidthHeightPx() * 0.8F
        val height = width * 3F / 4F
        val context = ContextThemeWrapper(context, R.style.AppTheme)
        val floatingView = FloatingView(context)
        floatingView.setListener(createFloatingView())
        floatingView.load(url)
        floatingView.setExpandedSize(width, height)
        floatingView.setCollapsedSize(ViewUtils.dpToPx(190F), ViewUtils.dpToPx(54F))
        val flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PRIORITY_PHONE
        }
        val layoutParams = WindowManager.LayoutParams(
                width.toInt(),
                height.toInt(),
                type,
                flags,
                PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(
                floatingView,
                layoutParams
        )
        floatingViews.add(floatingView)
    }

    override fun stop() {
        for (floatingView in floatingViews) {
            floatingView.close()
        }
        floatingViews.clear()
    }

    private fun getScreenMinWidthHeightPx(): Int {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        return Math.min(point.x, point.y)
    }

    private fun createFloatingView() = object : FloatingView.Listener {
        override fun onRemovedFromWindowManager(floatingView: FloatingView) {
            floatingViews.remove(floatingView)
        }
    }
}