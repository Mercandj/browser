package com.mercandalli.android.browser.analytics

interface AnalyticsManager {

    fun disable()

    fun sendEventOnBoardingStarted()

    fun sendEventOnBoardingTryDarkTheme()

    fun sendEventOnBoardingTryFullVersion()

    fun sendEventOnBoardingSubscriptionClicked()

    fun sendEventOnBoardingSubscriptionSwiped()

    fun sendEventOnBoardingSkipClicked()

    fun sendEventOnBoardingSubscribed()
}