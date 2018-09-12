package com.mercandalli.android.libs.monetization.store

import com.mercandalli.android.libs.monetization.Monetization
import com.mercandalli.android.libs.monetization.in_app.InAppManager

internal interface StoreContract {

    interface UserAction {

        fun onCreate(monetization: Monetization)

        fun onDestroy()

        fun onBuySubscriptionClicked(activityContainer: InAppManager.ActivityContainer)
    }

    interface Screen {

    }
}