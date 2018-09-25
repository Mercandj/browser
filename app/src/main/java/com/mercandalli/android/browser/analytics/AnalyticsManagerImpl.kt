package com.mercandalli.android.browser.analytics

class AnalyticsManagerImpl(
        private val addOn: AddOn
) : AnalyticsManager {

    override fun sendEventOnBoardingStarted() {
        addOn.logEvent("on_boarding_started")
    }

    override fun sendEventOnBoardingTryDarkTheme() {
        addOn.logEvent("on_boarding_try_dark_theme")
    }

    override fun sendEventOnBoardingTryFullVersion() {
        addOn.logEvent("on_boarding_try_full_version")
    }

    override fun sendEventOnBoardingSubscriptionClicked() {
        addOn.logEvent("on_boarding_subscription_clicked")
    }

    override fun sendEventOnBoardingSkipClicked() {
        addOn.logEvent("on_boarding_skip_clicked")
    }

    override fun sendEventOnBoardingSubscribed() {
        addOn.logEvent("on_boarding_subscribed")
    }

    interface AddOn {
        fun logEvent(eventKey: String)
    }
}