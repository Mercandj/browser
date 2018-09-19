package com.mercandalli.android.browser.in_app

import android.app.Activity
import android.content.Context

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener

/* package */ internal class PlayBillingManagerImpl(
       private val context: Context
) : PlayBillingManager {

    /**
     * True if billing service is connected now.
     */
    private var isServiceConnected: Boolean = false

    /**
     * Entry point to do some operations with in-apps inside the application.
     */
    private var billingClient: BillingClient? = null

    private var listener: PlayBillingManager.Listener? = null


    private val purchasesUpdatedListener: PurchasesUpdatedListener

    private val consumeResponseListener: ConsumeResponseListener

    init {
        purchasesUpdatedListener = createPurchaseUpdatedListener()
        consumeResponseListener = createConsumeResponseListener()
        isServiceConnected = false
    }

    override fun setUpPlayBilling() {
        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .build()
    }

    override fun release() {
        if (billingClient != null && billingClient!!.isReady) {
            billingClient!!.endConnection()
            billingClient = null
        }
    }

    override fun executeServiceRequest(runnable: Runnable) {
        if (isServiceConnected) {
            runnable.run()
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            startServiceConnection(runnable)
        }
    }

    override fun setPlayBillingManagerListener(listener: PlayBillingManager.Listener) {
        this.listener = listener
    }

    override fun querySkuDetailsAsync(
            build: SkuDetailsParams,
            skuDetailsResponseListener: SkuDetailsResponseListener) {
        checkBillingClientSetUp()
        billingClient!!.querySkuDetailsAsync(build, skuDetailsResponseListener)
    }

    override fun launchBillingFlow(activity: Activity, build: BillingFlowParams): Int {
        checkBillingClientSetUp()
        return billingClient!!.launchBillingFlow(activity, build)
    }

    override fun consumeInApp(purchaseToken: String) {
        billingClient!!.consumeAsync(purchaseToken, consumeResponseListener)
    }

    override fun queryPurchases(@BillingClient.SkuType sku: String): Purchase.PurchasesResult {
        checkBillingClientSetUp()
        return billingClient!!.queryPurchases(sku)
    }

    private fun startServiceConnection(executeOnSuccess: Runnable?) {
        checkBillingClientSetUp()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                    @BillingClient.BillingResponse billingResponseCode: Int) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    isServiceConnected = true
                    executeOnSuccess?.run()
                } else {
                    if (listener != null) {
                        listener!!.connectionToServiceFailed()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                isServiceConnected = false
            }
        })
    }

    private fun checkBillingClientSetUp() {
        if (billingClient == null) {
            throw IllegalStateException(
                    "You should set up PlayBillingManager before calling this method")
        }
    }

    private fun createConsumeResponseListener(): ConsumeResponseListener {
        return ConsumeResponseListener { responseCode, purchaseToken ->
            if (listener == null) {
                return@ConsumeResponseListener
            }
            if (responseCode == BillingClient.BillingResponse.OK) {
                listener!!.onInAppConsumed(purchaseToken)
            }
        }
    }

    private fun createPurchaseUpdatedListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { responseCode, purchases ->
            if (listener == null) {
                return@PurchasesUpdatedListener
            }

            if (responseCode == BillingClient.BillingResponse.OK) {
                listener!!.onPurchasesUpdated(purchases)
            }
        }
    }
}

