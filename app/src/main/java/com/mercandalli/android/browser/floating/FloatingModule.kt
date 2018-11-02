package com.mercandalli.android.browser.floating

import android.content.Context
import android.view.WindowManager

class FloatingModule(
    private val context: Context
) {

    private var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun createFloatingManager(): FloatingManager {
        val floatingPermission = createFloatingPermission()
        return FloatingManagerImpl(
            context,
            floatingPermission,
            windowManager
        )
    }

    private fun createFloatingPermission(): FloatingPermission {
        return FloatingPermissionImpl(context)
    }
}
