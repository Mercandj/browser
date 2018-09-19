package com.mercandalli.android.browser.in_app

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

interface InAppManager {

    fun initialize()

    fun purchase(
            activityContainer: ActivityContainer,
            sku: String,
            @BillingClient.SkuType skuType: String
    )

    fun requestSkuDetails(
            activityContainer: ActivityContainer,
            sku: String,
            @BillingClient.SkuType skuType: String
    )

    fun isPurchased(sku: String): Boolean

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)

    interface Listener {

        fun onSkuDetailsChanged(skuDetails: SkuDetails)

        fun onPurchasedChanged()
    }

    interface ActivityContainer {
        fun get(): Activity
    }
}