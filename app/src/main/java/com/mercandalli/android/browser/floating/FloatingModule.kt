package com.mercandalli.android.browser.floating

import android.content.Context

class FloatingModule(
        private val context: Context
) {

    fun createFloatingManager(): FloatingManager {
        val floatingPermission = createFloatingPermission()
        return FloatingManagerImpl(
                context,
                floatingPermission
        )
    }

    private fun createFloatingPermission(): FloatingPermission {
        return FloatingPermissionImpl(context)
    }
}