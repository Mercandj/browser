package com.mercandalli.android.browser.analytics

interface AnalyticsManager {

    fun sendEventOnBoardingStarted()

    fun sendEventOnBoardingTryDarkTheme()

    fun sendEventOnBoardingTryFullVersion()

    fun sendEventOnBoardingSubscriptionClicked()

    fun sendEventOnBoardingSkipClicked()
}