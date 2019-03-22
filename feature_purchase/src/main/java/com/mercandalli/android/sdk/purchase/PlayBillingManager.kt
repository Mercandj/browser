@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.purchase

import android.app.Activity
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

internal interface PlayBillingManager {

    fun setUpPlayBilling()

    fun release()

    fun executeServiceRequest(
        runnable: Runnable
    )

    fun setPlayBillingManagerListener(
        listener: Listener
    )

    fun querySkuDetailsAsync(
        build: SkuDetailsParams,
        skuDetailsResponseListener: SkuDetailsResponseListener
    )

    fun launchBillingFlow(
        activity: Activity,
        build: BillingFlowParams
    ): Int

    fun consumeInApp(
        purchaseToken: String
    )

    fun queryPurchases(
        @BillingClient.SkuType sku: String
    ): Purchase.PurchasesResult

    /**
     * Listener to notify when a purchase has been done or when the service has been disconnected.
     */
    interface Listener {

        fun onPurchasesUpdated(purchases: List<Purchase>?)

        fun onInAppConsumed(purchaseToken: String)

        fun connectionToServiceFailed()
    }
}
