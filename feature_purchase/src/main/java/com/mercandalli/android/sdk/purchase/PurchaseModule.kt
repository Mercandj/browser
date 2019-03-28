@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.purchase

import android.content.Context
import com.mercandalli.android.sdk.purchase.internal.PlayBillingManagerImpl
import com.mercandalli.android.sdk.purchase.internal.PurchaseManagerImpl
import com.mercandalli.android.sdk.purchase.internal.PurchaseRepository
import com.mercandalli.android.sdk.purchase.internal.PurchaseRepositoryImpl

class PurchaseModule(
    private val context: Context
) {

    fun createPurchaseManager(
        purchaseAnalyticsManager: PurchaseAnalyticsManager? = null
    ): PurchaseManager {
        val playBillingManager = createPlayBillingManager()
        val purchaseRepository = createPurchaseRepository()
        return PurchaseManagerImpl(
            playBillingManager,
            purchaseRepository,
            purchaseAnalyticsManager
        )
    }

    private fun createPlayBillingManager() = PlayBillingManagerImpl(
        context.applicationContext
    )

    private fun createPurchaseRepository(): PurchaseRepository {
        val sharedPreferences = context.getSharedPreferences(
            PurchaseRepositoryImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return PurchaseRepositoryImpl(
            sharedPreferences
        )
    }
}
