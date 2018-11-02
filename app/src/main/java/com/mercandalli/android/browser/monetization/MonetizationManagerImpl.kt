package com.mercandalli.android.browser.monetization

internal class MonetizationManagerImpl : MonetizationManager {

    private var monetizationEnabled = false
    private val monetizationEnabledListeners = ArrayList<MonetizationManager.MonetizationListener>()

    override fun isOnBoardingStorePageAvailable() = monetizationEnabled

    override fun setOnBoardingStorePageAvailable(enable: Boolean) {
        monetizationEnabled = enable
        for (listener in monetizationEnabledListeners) {
            listener.onOnBoardingStorePageAvailableChanged()
        }
    }

    override fun registerMonetizationListener(
        listener: MonetizationManager.MonetizationListener
    ) {
        if (monetizationEnabledListeners.contains(listener)) {
            return
        }
        monetizationEnabledListeners.add(listener)
    }

    override fun unregisterMonetizationListener(
        listener: MonetizationManager.MonetizationListener
    ) {
        monetizationEnabledListeners.remove(listener)
    }
}
