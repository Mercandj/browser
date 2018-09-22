package com.mercandalli.android.browser.product

import android.content.SharedPreferences
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.mercandalli.android.browser.main.MainApplication
import com.mercandalli.android.browser.remote_config.RemoteConfig
import com.mercandalli.android.browser.in_app.InAppManager

class ProductManagerImpl(
        private val remoteConfig: RemoteConfig,
        private val inAppManager: InAppManager,
        private val sharedPreferences: SharedPreferences
) : ProductManager {

    private val listeners = ArrayList<ProductManager.Listener>()
    private val appDeveloperListeners = ArrayList<ProductManager.AppDeveloperListener>()
    private var isAppDeveloperEnabled: Boolean = false

    init {
        inAppManager.registerListener(createInAppListener())
        isAppDeveloperEnabled = sharedPreferences.getBoolean(PREFERENCE_IS_APP_DEVELOPER_ENABLED, isAppDeveloperEnabled)
    }

    override fun purchaseFullVersion(activityContainer: InAppManager.ActivityContainer) {
        inAppManager.purchase(
                activityContainer,
                MainApplication.SKU_SUBSCRIPTION_FULL_VERSION,
                BillingClient.SkuType.SUBS
        )
    }

    override fun isFullVersionAvailable() = isAppDeveloperEnabled ||
            remoteConfig.isFullVersionAvailable

    override fun isSubscribeToFullVersion() = isAppDeveloperEnabled ||
            inAppManager.isPurchased(MainApplication.SKU_SUBSCRIPTION_FULL_VERSION)

    override fun isAppDeveloperEnabled() = isAppDeveloperEnabled

    override fun setIsAppDeveloperEnabled(enabled: Boolean) {
        isAppDeveloperEnabled = enabled
        sharedPreferences.edit().putBoolean(
                PREFERENCE_IS_APP_DEVELOPER_ENABLED,
                enabled
        ).apply()
        for (listener in appDeveloperListeners) {
            listener.onIsAppDeveloperChanged()
        }
        for (listener in listeners) {
            listener.onSubscribeToFullVersionChanged()
        }
    }

    override fun registerListener(listener: ProductManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterListener(listener: ProductManager.Listener) {
        listeners.remove(listener)
    }

    override fun registerAppDeveloperListener(listener: ProductManager.AppDeveloperListener) {
        if (appDeveloperListeners.contains(listener)) {
            return
        }
        appDeveloperListeners.add(listener)
    }

    override fun unregisterAppDeveloperListener(listener: ProductManager.AppDeveloperListener) {
        appDeveloperListeners.remove(listener)
    }

    private fun createInAppListener() = object : InAppManager.Listener {
        override fun onPurchasedChanged() {
            for (listener in listeners) {
                listener.onSubscribeToFullVersionChanged()
            }
        }

        override fun onSkuDetailsChanged(skuDetails: SkuDetails) {}
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "ProductManager"
        @JvmStatic
        private val PREFERENCE_IS_APP_DEVELOPER_ENABLED = "is-add-developer-enabled"
    }
}