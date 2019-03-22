@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.purchase

import android.content.Context

class PurchaseModule(
    private val context: Context
) {

    fun createPurchaseManager(
        purchaseAnalyticsManager: PurchaseAnalyticsManager? = null
    ): PurchaseManager {
        val playBillingManager = createPlayBillingManager()
        val inAppRepository = createInAppRepository()
        return PurchaseManagerImpl(
            playBillingManager,
            inAppRepository,
            purchaseAnalyticsManager
        )
    }

    private fun createPlayBillingManager() = PlayBillingManagerImpl(context.applicationContext)

    private fun createInAppRepository(): PurchaseRepository {
        val sharedPreferences = context.getSharedPreferences(
            PurchaseRepositoryImpl.PREFERENCE_NAME, Context.MODE_PRIVATE
        )
        return PurchaseRepositoryImpl(
            sharedPreferences
        )
    }
}
