package com.mercandalli.android.browser.floating

interface FloatingPermission {

    /**
     * Can this app be over other app (with the [android.view.WindowMananager])
     */
    fun canDrawOverOtherApps(): Boolean

    /**
     * Launch the settings to manage the permission to draw over other apps.
     *
     * Before [android.os.Build.VERSION_CODES.M], this is a no-op call.
     */
    fun launchDrawOverOtherAppPermissionManager()
}
