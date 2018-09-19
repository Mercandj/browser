package com.mercandalli.android.browser.floating

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

class FloatingPermissionImpl(
        private val context: Context
) : FloatingPermission {

    override fun canDrawOverOtherApps() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
    else Settings.canDrawOverlays(context)

    @SuppressLint("InlinedApi")
    override fun launchDrawOverOtherAppPermissionManager() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName))
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(intent)
    }
}