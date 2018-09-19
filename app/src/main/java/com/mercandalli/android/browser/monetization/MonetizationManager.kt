package com.mercandalli.android.browser.monetization

internal interface MonetizationManager {

    fun isOnBoardingStorePageAvailable(): Boolean

    fun setOnBoardingStorePageAvailable(enable: Boolean)

    fun registerMonetizationListener(listener: MonetizationListener)

    fun unregisterMonetizationListener(listener: MonetizationListener)

    interface MonetizationListener {

        fun onOnBoardingStorePageAvailableChanged()
    }
}