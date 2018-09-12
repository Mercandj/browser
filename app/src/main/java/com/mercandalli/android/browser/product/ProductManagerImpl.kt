package com.mercandalli.android.browser.product

import com.mercandalli.android.browser.main.MainApplication
import com.mercandalli.android.browser.remote_config.RemoteConfig
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class ProductManagerImpl(
        private val remoteConfig: RemoteConfig,
        private val inAppManager: InAppManager
) : ProductManager {

    override fun isFullVersionAvailable() = remoteConfig.isFullVersionAvailable

    override fun isSubscribeToFullVersion() = inAppManager.isPurchased(MainApplication.SKU_SUBSCRIPTION_FULL_VERSION)
}