package com.mercandalli.android.browser.floating

import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.graphics.Point
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import com.mercandalli.android.browser.R

class FloatingManagerImpl(
        private val context: Context,
        private val floatingPermission: FloatingPermission,
        private val windowManager: WindowManager
) : FloatingManager {

    override fun start(url: String) {
        if (floatingPermission.cannotDrawOverOtherApps()) {
            floatingPermission.launchDrawOverOtherAppPermissionManager()
            return
        }
        val context = ContextThemeWrapper(context, R.style.AppTheme)
        val mainWebView = FloatingView(context)
        mainWebView.load(url)
        val width = getScreenMinWidthHeightPx() * 0.8F
        val height = width * 3F / 4F
        val flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        val layoutParams = WindowManager.LayoutParams(
                width.toInt(),
                height.toInt(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                flags,
                PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(
                mainWebView,
                layoutParams
        )
    }

    private fun getScreenMinWidthHeightPx(): Int {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        return Math.min(point.x, point.y)
    }
}