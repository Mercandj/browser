package com.mercandalli.android.browser.product

interface ProductManager {

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