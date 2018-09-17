package com.mercandalli.android.browser.floating

interface FloatingPermission {

    fun cannotDrawOverOtherApps(): Boolean

    /**
     * Launch the settings to manage the permission to draw over other apps.
     *
     * Before [android.os.Build.VERSION_CODES.M], this is a no-op call.
     */
    fun launchDrawOverOtherAppPermissionManager()
}