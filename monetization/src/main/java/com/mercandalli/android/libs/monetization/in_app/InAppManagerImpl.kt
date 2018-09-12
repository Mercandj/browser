package com.mercandalli.android.libs.monetization.in_app

import com.android.billingclient.api.*
import com.mercandalli.android.libs.monetization.log.MonetizationLog

internal class InAppManagerImpl(
        private val playBillingManager: PlayBillingManager,
        private val inAppRepository: InAppRepository,
        private val monetizationLog: MonetizationLog
) : InAppManager {

    private var initializeCalled = false
    private val skuDetailsResponseListener = createSkuDetailsResponseListener()
    private val playBillingManagerListener = createPlayBillingManagerListener()
    private val listeners = ArrayList<InAppManager.Listener>()

    override fun initialize() {
        if (initializeCalled) {
            return
        }
        initializeCalled = true
        playBillingManager.setUpPlayBilling()
        playBillingManager.setPlayBillingManagerListener(playBillingManagerListener)
    }

    override fun purchase(
            activityContainer: InAppManager.ActivityContainer,
            sku: String,
            @BillingClient.SkuType skuType: String
    ) {
        playBillingManager.executeServiceRequest {
            val builder = BillingFlowParams
                    .newBuilder()
                    .setSku(sku)
                    .setType(skuType)
            val responseCode = playBillingManager.launchBillingFlow(activityContainer.get(), builder.build())
            if (responseCode == BillingClient.BillingResponse.OK) {
                monetizationLog.d(TAG, "responseCode: $responseCode")
                return@executeServiceRequest
            }
            if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                monetizationLog.d(TAG, "User already owns this in-app: $sku")
            } else {
                monetizationLog.d(TAG, "An error occurred during purchase, error code: $responseCode")
            }
        }
    }

    override fun requestSkuDetails(
            activityContainer: InAppManager.ActivityContainer,
            sku: String,
            @BillingClient.SkuType skuType: String
    ) {
        playBillingManager.executeServiceRequest {
            val subsSkuDetailsParams = SkuDetailsParams.newBuilder()
                    .setSkusList(listOf(
                            sku
                    ))
                    .setType(skuType)
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
    }

    override fun isPurchased(sku: String) = true//inAppRepository.isPurchased(sku)

    override fun registerListener(listener: InAppManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterListener(listener: InAppManager.Listener) {
        listeners.remove(listener)
    }

    private fun notifySkuDetailsChanged(skuDetails: SkuDetails) {
        for (listener in listeners) {
            monetizationLog.d(TAG, "" + skuDetails.sku + " " + skuDetails.price)
            listener.onSkuDetailsChanged(skuDetails)
        }
    }

    private fun notifyPurchasedChanged() {
        for (listener in listeners) {
            listener.onPurchasedChanged()
        }
    }

    private fun createSkuDetailsResponseListener() = SkuDetailsResponseListener { responseCode, skuDetailsList ->
        for (skuDetails in skuDetailsList) {
            notifySkuDetailsChanged(skuDetails)
        }
    }

    private fun createPlayBillingManagerListener() = object : PlayBillingManager.Listener {
        override fun onPurchasesUpdated(@org.jetbrains.annotations.Nullable purchases: List<Purchase>?) {
            if (purchases == null) {
                return
            }
            monetizationLog.d(TAG, "Purchase succeed")
            for (purchase in purchases) {
                inAppRepository.addPurchased(purchase.sku)
                notifyPurchasedChanged()
                monetizationLog.d(TAG, "Purchase succeed: " + purchase.purchaseToken)
            }
        }

        override fun onInAppConsumed(purchaseToken: String) {
            monetizationLog.d(TAG, "In-app has been consumed")
        }

        override fun connectionToServiceFailed() {
            monetizationLog.d(TAG, "Connection to Play Store has failed")
        }
    }

    companion object {
        private const val TAG = "InAppManager"
    }
}