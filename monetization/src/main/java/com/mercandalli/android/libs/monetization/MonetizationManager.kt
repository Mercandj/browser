package com.mercandalli.android.libs.monetization

internal interface MonetizationManager {

    fun isOnBoardingStorePageAvailable(): Boolean

    fun setOnBoardingStorePageAvailable(enable: Boolean)

    fun registerMonetizationListener(listener: MonetizationListener)

    fun unregisterMonetizationListener(listener: MonetizationListener)

    interface MonetizationListener {

        fun onOnBoardingStorePageAvailableChanged()
    }
}