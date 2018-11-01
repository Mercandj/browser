package com.mercandalli.android.browser.analytics

class AnalyticsManagerImpl(
        private val addOn: AddOn
) : AnalyticsManager {

    private var enabled = true

    override fun disable() {
        enabled = false
    }

    override fun sendEventOnBoardingStarted() {
        logEvent("on_boarding_started")
    }

    override fun sendEventOnBoardingTryDarkTheme() {
        logEvent("on_boarding_try_dark_theme")
    }

    override fun sendEventOnBoardingTryFullVersion() {
        logEvent("on_boarding_try_full_version")
    }

    override fun sendEventOnBoardingSubscriptionClicked() {
        logEvent("on_boarding_subscription_clicked")
    }

    override fun sendEventOnBoardingSubscriptionSwiped() {
        logEvent("on_boarding_subscription_swiped")
    }

    override fun sendEventOnBoardingSkipClicked() {
        logEvent("on_boarding_skip_clicked")
    }

    override fun sendEventOnBoardingSubscribed() {
        logEvent("on_boarding_subscribed")
    }

    private fun logEvent(eventKey: String) {
        if (enabled) {
            addOn.logEvent(eventKey)
        }
    }

    interface AddOn {
        fun logEvent(eventKey: String)
    }
}