package com.mercandalli.android.browser.product

import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.libs.monetization.MonetizationGraph

class ProductModule {

    fun createProductManager(): ProductManager {
        val remoteConfig = ApplicationGraph.getRemoteConfig()
        val inAppManager = MonetizationGraph.getInAppManager()
        return ProductManagerImpl(
                remoteConfig,
                inAppManager
        )
    }
}