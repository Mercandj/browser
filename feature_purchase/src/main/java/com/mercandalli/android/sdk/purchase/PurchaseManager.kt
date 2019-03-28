@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.purchase

import android.app.Activity
import androidx.annotation.StringDef
import com.android.billingclient.api.BillingClient

interface PurchaseManager {

    fun initialize()

    fun purchase(
        activityContainer: ActivityContainer,
        sku: String,
        @SkuType skuType: String
    )

    fun requestSkuDetails(
        sku: String,
        @SkuType skuType: String
    )

    fun isPurchased(
        sku: String
    ): Boolean

    fun isPurchasedEmpty(): Boolean

    fun registerListener(
        listener: Listener
    )

    fun unregisterListener(
        listener: Listener
    )

    interface Listener {

        fun onSkuDetailsChanged(purchaseDetails: PurchaseDetails)

        fun onPurchasedChanged()
    }

    interface ActivityContainer {

        fun get(): Activity
    }

    companion object {

        @StringDef(
            INAPP,
            SUBS
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class SkuType

        const val INAPP = BillingClient.SkuType.INAPP
        const val SUBS = BillingClient.SkuType.SUBS
    }
}
