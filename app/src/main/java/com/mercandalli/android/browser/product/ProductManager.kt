package com.mercandalli.android.browser.product

interface ProductManager {

    fun isFullVersionAvailable(): Boolean

    fun isSubscribeToFullVersion(): Boolean

    fun isAppDeveloperEnabled(): Boolean

    fun setIsAppDeveloperEnabled(enabled: Boolean)

    fun registerAppDeveloperListener(listener: AppDeveloperListener)

    fun unregisterAppDeveloperListener(listener: AppDeveloperListener)

    interface AppDeveloperListener {

        fun onIsAppDeveloperChanged()
    }
}