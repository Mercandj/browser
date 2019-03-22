package com.mercandalli.android.browser.product

import android.content.Context
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.monetization.MonetizationGraph

class ProductModule(
    private val context: Context
) {

    fun createProductManager(): ProductManager {
        val remoteConfig = ApplicationGraph.getRemoteConfig()
        val purchaseManager = MonetizationGraph.getPurchaseManager()
        val sharedPreferences = context.getSharedPreferences(
            ProductManagerImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return ProductManagerImpl(
            remoteConfig,
            purchaseManager,
            sharedPreferences
        )
    }
}
