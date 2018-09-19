package com.mercandalli.android.browser.store

import com.android.billingclient.api.BillingClient
import com.mercandalli.android.browser.monetization.Monetization
import com.mercandalli.android.browser.in_app.InAppManager

internal class StorePresenter(
        private val screen: StoreContract.Screen,
        private val inAppManager: InAppManager
) : StoreContract.UserAction {

    private lateinit var monetization: Monetization

    override fun onCreate(monetization: Monetization) {
        this.monetization = monetization
        inAppManager.initialize()
    }

    override fun onDestroy() {

    }

    override fun onBuySubscriptionClicked(activityContainer: InAppManager.ActivityContainer) {
        inAppManager.purchase(activityContainer, monetization.subscriptionSku, BillingClient.SkuType.SUBS)
    }
}