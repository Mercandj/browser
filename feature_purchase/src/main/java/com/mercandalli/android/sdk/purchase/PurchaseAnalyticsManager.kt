package com.mercandalli.android.sdk.purchase

interface PurchaseAnalyticsManager {

    fun sendEventPurchaseFeatureNotSupported(sku: String)

    fun sendEventPurchaseServiceDisconnected(sku: String)

    fun sendEventPurchaseOk(sku: String)

    fun sendEventPurchaseUserCanceled(sku: String)

    fun sendEventPurchaseServiceUnavailable(sku: String)

    fun sendEventPurchaseBillingUnavailable(sku: String)

    fun sendEventPurchaseItemUnavailable(sku: String)

    fun sendEventPurchaseDeveloperError(sku: String)

    fun sendEventPurchaseError(sku: String)

    fun sendEventPurchaseItemAlreadyOwned(sku: String)

    fun sendEventPurchaseItemNotOwned(sku: String)

    fun sendEventPurchased(sku: String)
}
