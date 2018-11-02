@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.remote_config

interface RemoteConfig {

    fun isFullVersionAvailable(): Boolean

    fun isOnBoardingStoreAvailable(): Boolean

    fun getSubscriptionFullVersionSku(): String

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)

    interface Listener {

        /***
         * Called when values of getters values changed.
         */
        fun onRemoteConfigChanged()
    }
}
