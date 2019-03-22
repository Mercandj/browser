package com.mercandalli.android.sdk.purchase

@Suppress("unused")
class PurchaseAnalyticsManagerImpl(
    private val addOn: AddOn
) : PurchaseAnalyticsManager {

    override fun sendEventPurchaseFeatureNotSupported(sku: String) {
        logEvent("purchase_feature_not_supported")
    }

    override fun sendEventPurchaseServiceDisconnected(sku: String) {
        logEvent("purchase_service_disconnected")
    }

    override fun sendEventPurchaseOk(sku: String) {
        logEvent("purchase_ok")
    }

    override fun sendEventPurchaseUserCanceled(sku: String) {
        logEvent("purchase_user_canceled")
    }

    override fun sendEventPurchaseServiceUnavailable(sku: String) {
        logEvent("purchase_service_unavailable")
    }

    override fun sendEventPurchaseBillingUnavailable(sku: String) {
        logEvent("purchase_billing_unavailable")
    }

    override fun sendEventPurchaseItemUnavailable(sku: String) {
        logEvent("purchase_item_unavailable")
    }

    override fun sendEventPurchaseDeveloperError(sku: String) {
        logEvent("purchase_developer_error")
    }

    override fun sendEventPurchaseError(sku: String) {
        logEvent("purchase_error")
    }

    override fun sendEventPurchaseItemAlreadyOwned(sku: String) {
        logEvent("purchase_item_already_owned")
    }

    override fun sendEventPurchaseItemNotOwned(sku: String) {
        logEvent("purchase_item_not_owned")
    }

    override fun sendEventPurchased(sku: String) {
        logEvent("purchased")
    }

    private fun logEvent(eventKey: String) {
        addOn.logEvent(eventKey)
    }

    interface AddOn {

        fun logEvent(eventKey: String)
    }
}
