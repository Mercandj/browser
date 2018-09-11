package com.mercandalli.android.libs.monetization.in_app;

import android.app.Activity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.List;

import androidx.annotation.Nullable;

interface PlayBillingManager {

    void setUpPlayBilling();

    void release();

    void executeServiceRequest(
            Runnable runnable);

    void setPlayBillingManagerListener(
            Listener listener);

    void querySkuDetailsAsync(
            SkuDetailsParams build,
            SkuDetailsResponseListener skuDetailsResponseListener);

    int launchBillingFlow(
            Activity activity,
            BillingFlowParams build);

    void consumeInApp(
            String purchaseToken);

    Purchase.PurchasesResult queryPurchases(
            @BillingClient.SkuType String sku);

    /**
     * Listener to notify when a purchase has been done or when the service has been disconnected.
     */
    interface Listener {

        void onPurchasesUpdated(@Nullable List<Purchase> purchases);

        void onInAppConsumed(String purchaseToken);

        void connectionToServiceFailed();
    }
}
