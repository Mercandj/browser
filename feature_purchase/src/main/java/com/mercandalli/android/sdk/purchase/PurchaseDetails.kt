package com.mercandalli.android.sdk.purchase

import com.android.billingclient.api.SkuDetails

data class PurchaseDetails internal constructor(
    val sku: String,
    val price: String
) {

    companion object {

        internal fun create(
            skuDetails: SkuDetails
        ): PurchaseDetails {
            val sku = skuDetails.sku
            val price = skuDetails.price
            return PurchaseDetails(
                sku,
                price
            )
        }
    }
}
