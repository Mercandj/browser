@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.purchase.internal

import androidx.annotation.Nullable
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetailsParams
import com.mercandalli.android.sdk.purchase.PurchaseAnalyticsManager
import com.mercandalli.android.sdk.purchase.PurchaseDetails
import com.mercandalli.android.sdk.purchase.PurchaseManager
import java.lang.IllegalStateException

internal class PurchaseManagerImpl(
    private val playBillingManager: PlayBillingManager,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseAnalyticsManager: PurchaseAnalyticsManager?
) : PurchaseManager {

    private var initializeCalled = false
    private val skuDetailsResponseListener = createSkuDetailsResponseListener()
    private val playBillingManagerListener = createPlayBillingManagerListener()
    private val listeners = ArrayList<PurchaseManager.Listener>()

    override fun initialize() {
        if (initializeCalled) {
            return
        }
        initializeCalled = true
        playBillingManager.setUpPlayBilling()
        playBillingManager.setPlayBillingManagerListener(playBillingManagerListener)
        restore()
    }

    override fun purchase(
        activityContainer: PurchaseManager.ActivityContainer,
        sku: String,
        @PurchaseManager.Companion.SkuType skuType: String
    ) {
        if (purchaseRepository.isPurchased(sku)) {
            notifyPurchasedChanged()
            return
        }
        val skuTypeGoogle = convertSkuType(skuType)
        val runnable = Runnable {
            val builder = BillingFlowParams
                .newBuilder()
                .setSku(sku)
                .setType(skuTypeGoogle)
            val responseCode = playBillingManager.launchBillingFlow(
                activityContainer.get(),
                builder.build()
            )
            when (responseCode) {
                BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED -> purchaseAnalyticsManager?.sendEventPurchaseFeatureNotSupported(
                    sku
                )
                BillingClient.BillingResponse.SERVICE_DISCONNECTED -> purchaseAnalyticsManager?.sendEventPurchaseServiceDisconnected(
                    sku
                )
                BillingClient.BillingResponse.OK -> purchaseAnalyticsManager?.sendEventPurchaseOk(sku)
                BillingClient.BillingResponse.USER_CANCELED -> purchaseAnalyticsManager?.sendEventPurchaseUserCanceled(sku)
                BillingClient.BillingResponse.SERVICE_UNAVAILABLE -> purchaseAnalyticsManager?.sendEventPurchaseServiceUnavailable(
                    sku
                )
                BillingClient.BillingResponse.BILLING_UNAVAILABLE -> purchaseAnalyticsManager?.sendEventPurchaseBillingUnavailable(
                    sku
                )
                BillingClient.BillingResponse.ITEM_UNAVAILABLE -> purchaseAnalyticsManager?.sendEventPurchaseItemUnavailable(
                    sku
                )
                BillingClient.BillingResponse.DEVELOPER_ERROR -> purchaseAnalyticsManager?.sendEventPurchaseDeveloperError(
                    sku
                )
                BillingClient.BillingResponse.ERROR -> purchaseAnalyticsManager?.sendEventPurchaseError(sku)
                BillingClient.BillingResponse.ITEM_ALREADY_OWNED -> purchaseAnalyticsManager?.sendEventPurchaseItemAlreadyOwned(
                    sku
                )
                BillingClient.BillingResponse.ITEM_NOT_OWNED -> purchaseAnalyticsManager?.sendEventPurchaseItemNotOwned(sku)
            }
        }
        playBillingManager.executeServiceRequest(runnable)
    }

    override fun requestSkuDetails(
        sku: String,
        @BillingClient.SkuType skuType: String
    ) {
        val skuTypeGoogle = convertSkuType(skuType)
        val runnable = Runnable {
            val subsSkuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(
                    listOf(
                        sku
                    )
                )
                .setType(skuTypeGoogle)
                .build()
            playBillingManager.querySkuDetailsAsync(
                subsSkuDetailsParams,
                skuDetailsResponseListener
            )

            val inAppPurchasesResult = playBillingManager.queryPurchases(BillingClient.SkuType.INAPP)
            inAppPurchasesResult.purchasesList

            val subsPurchasesResult = playBillingManager.queryPurchases(BillingClient.SkuType.SUBS)
            subsPurchasesResult.purchasesList
        }
        playBillingManager.executeServiceRequest(runnable)
    }

    override fun isPurchased(
        sku: String
    ) = purchaseRepository.isPurchased(sku)

    override fun isPurchasedEmpty() = purchaseRepository.isEmpty()

    override fun registerListener(
        listener: PurchaseManager.Listener
    ) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterListener(
        listener: PurchaseManager.Listener
    ) {
        listeners.remove(listener)
    }

    private fun restore() {
        val runnable = Runnable {
            var purchaseAdded = false
            val inAppPurchasesResult = playBillingManager.queryPurchases(
                BillingClient.SkuType.INAPP
            )
            inAppPurchasesResult.purchasesList?.let {
                for (purchase in it) {
                    if (purchaseRepository.addPurchased(purchase.sku)) {
                        purchaseAdded = true
                    }
                }
            }
            val subsPurchasesResult = playBillingManager.queryPurchases(
                BillingClient.SkuType.SUBS
            )
            subsPurchasesResult.purchasesList?.let {
                for (purchase in it) {
                    if (purchaseRepository.addPurchased(purchase.sku)) {
                        purchaseAdded = true
                    }
                }
            }
            if (purchaseAdded) {
                notifyPurchasedChanged()
            }
        }
        playBillingManager.executeServiceRequest(runnable)
    }

    private fun notifySkuDetailsChanged(skuDetails: SkuDetails) {
        val purchaseDetails = PurchaseDetails.create(skuDetails)
        for (listener in listeners) {
            listener.onSkuDetailsChanged(purchaseDetails)
        }
    }

    private fun notifyPurchasedChanged() {
        for (listener in listeners) {
            listener.onPurchasedChanged()
        }
    }

    private fun createSkuDetailsResponseListener() = SkuDetailsResponseListener { _, skuDetailsList ->
        for (skuDetails in skuDetailsList) {
            notifySkuDetailsChanged(skuDetails)
        }
    }

    private fun createPlayBillingManagerListener() = object : PlayBillingManager.Listener {
        override fun onPurchasesUpdated(@Nullable purchases: List<Purchase>?) {
            if (purchases == null) {
                return
            }
            for (purchase in purchases) {
                val sku = purchase.sku
                val added = purchaseRepository.addPurchased(sku)
                if (added) {
                    purchaseAnalyticsManager?.sendEventPurchased(sku)
                    notifyPurchasedChanged()
                }
            }
        }

        override fun onInAppConsumed(purchaseToken: String) {
        }

        override fun connectionToServiceFailed() {
        }
    }

    companion object {

        private fun convertSkuType(
            @PurchaseManager.Companion.SkuType skuType: String
        ): String {
            return when (skuType) {
                PurchaseManager.INAPP -> BillingClient.SkuType.INAPP
                PurchaseManager.SUBS -> BillingClient.SkuType.SUBS
                else -> throw IllegalStateException("Wrong skuType: $skuType")
            }
        }
    }
}
