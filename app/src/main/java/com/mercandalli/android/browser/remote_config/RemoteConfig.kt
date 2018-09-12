package com.mercandalli.android.browser.remote_config

interface RemoteConfig {

    /**
     * @return true if the [RemoteConfig] is initialized, false otherwise
     */
    val isInitialized: Boolean

    val isFullVersionAvailable: Boolean

    val isOnBoardingStoreAvailable: Boolean

    fun registerListener(listener: RemoteConfigListener)

    fun unregisterListener(listener: RemoteConfigListener)

    interface RemoteConfigListener {

        /***
         * is triggered when the [RemoteConfig] is initialized
         */
        fun onInitialized()
    }
}
