package com.mercandalli.android.browser.product

import com.mercandalli.android.browser.in_app.InAppManager

interface ProductManager {

    fun purchaseFullVersion(
            activityContainer: InAppManager.ActivityContainer
    )

    fun isFullVersionAvailable(): Boolean

    fun isSubscribeToFullVersion(): Boolean

    fun isAppDeveloperEnabled(): Boolean

    fun setIsAppDeveloperEnabled(enabled: Boolean)

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)

    fun registerAppDeveloperListener(listener: AppDeveloperListener)

    fun unregisterAppDeveloperListener(listener: AppDeveloperListener)

    interface Listener {

        fun onSubscribeToFullVersionChanged()
    }

    interface AppDeveloperListener {

        fun onIsAppDeveloperChanged()
    }
}