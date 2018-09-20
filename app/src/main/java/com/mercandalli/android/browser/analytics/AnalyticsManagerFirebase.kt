package com.mercandalli.android.browser.analytics

import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManagerFirebase(
        private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsManager {

    override fun sendEventOnBoardingTryDarkTheme() {
        firebaseAnalytics.logEvent("on_boarding_try_dark_theme", null)
    }

    override fun sendEventOnBoardingTryFullVersion() {
        firebaseAnalytics.logEvent("on_boarding_try_full_version", null)
    }
}