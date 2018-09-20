package com.mercandalli.android.browser.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsModule(
        private val context: Context
) {

    fun createAnalyticsManager(): AnalyticsManager {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        return AnalyticsManagerFirebase(
                firebaseAnalytics
        )
    }
}
