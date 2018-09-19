package com.mercandalli.android.browser.store

import com.mercandalli.android.browser.monetization.Monetization
import com.mercandalli.android.browser.in_app.InAppManager

internal interface StoreContract {

    interface UserAction {

        fun onCreate(monetization: Monetization)

        fun onDestroy()

        fun onBuySubscriptionClicked(activityContainer: InAppManager.ActivityContainer)
    }

    interface Screen {

    }
}