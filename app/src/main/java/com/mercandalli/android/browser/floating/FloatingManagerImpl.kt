package com.mercandalli.android.browser.floating

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.ContextThemeWrapper
import android.view.WindowManager
import androidx.appcompat.widget.ViewUtils
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.MainWebView

class FloatingManagerImpl(
        private val context: Context
) : FloatingManager {

    private var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    override fun start(url: String) {
        if (cannotDrawOverOtherApps(context)) {
            launchDrawOverOtherAppPermissionManager(context)
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


    private fun cannotDrawOverOtherApps(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            false
        } else !Settings.canDrawOverlays(context)
    }


    /**
     * Launch the settings to manage the permission to draw over other apps.
     *
     *
     * Before [android.os.Build.VERSION_CODES.M], this is a no-op call.
     *
     * @param context a [Context].
     */
    @SuppressLint("InlinedApi")
    private fun launchDrawOverOtherAppPermissionManager(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName))
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(intent)
    }

}