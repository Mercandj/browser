package com.mercandalli.android.browser.analytics

import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManagerFirebase(
        private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsManager {

    override fun sendEventOnBoardingStarted() {
        firebaseAnalytics.logEvent("on_boarding_started", null)
    }

    override fun sendEventOnBoardingTryDarkTheme() {
        firebaseAnalytics.logEvent("on_boarding_try_dark_theme", null)
    }

    override fun sendEventOnBoardingTryFullVersion() {
        firebaseAnalytics.logEvent("on_boarding_try_full_version", null)
    }

    override fun sendEventOnBoardingSubscriptionClicked() {
        firebaseAnalytics.logEvent("on_boarding_subscription_clicked", null)
    }

    override fun sendEventOnBoardingSkipClicked() {
        firebaseAnalytics.logEvent("on_boarding_skip_clicked", null)
    }
}