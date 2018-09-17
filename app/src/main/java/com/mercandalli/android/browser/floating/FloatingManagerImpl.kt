package com.mercandalli.android.browser.floating

import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.view.ContextThemeWrapper
import android.view.WindowManager
import com.mercandalli.android.browser.R

class FloatingManagerImpl(
        private val context: Context,
        private val floatingPermission: FloatingPermission
) : FloatingManager {

    private var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    override fun start(url: String) {
        if (floatingPermission.cannotDrawOverOtherApps()) {
            floatingPermission.launchDrawOverOtherAppPermissionManager()
            return
        }
        val context = ContextThemeWrapper(context, R.style.AppTheme)
        val mainWebView = FloatingView(context)
        mainWebView.load(url)
        val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                dpToPx(300F).toInt(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT
        )
        windowManager.addView(
                mainWebView,
                layoutParams
        )
    }

    private fun dpToPx(dp: Float) = dp * Resources.getSystem().displayMetrics.density
}