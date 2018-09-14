package com.mercandalli.android.browser.product

import android.content.SharedPreferences
import com.mercandalli.android.browser.main.MainApplication
import com.mercandalli.android.browser.remote_config.RemoteConfig
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class ProductManagerImpl(
        private val remoteConfig: RemoteConfig,
        private val inAppManager: InAppManager,
        private val sharedPreferences: SharedPreferences
) : ProductManager {

    private val appDeveloperListeners = ArrayList<ProductManager.AppDeveloperListener>()
    private var isAppDeveloperEnabled: Boolean = false

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

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "ProductManager"
        @JvmStatic
        private val PREFERENCE_IS_APP_DEVELOPER_ENABLED = "is-add-developer-enabled"
    }
}