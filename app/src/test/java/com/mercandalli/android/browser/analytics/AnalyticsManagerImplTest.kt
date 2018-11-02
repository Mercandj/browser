package com.mercandalli.android.browser.analytics

import org.junit.Assert
import org.junit.Test

class AnalyticsManagerImplTest {

    @Test
    fun sendEventSendGoodKeys() {
        // Given
        val eventKeysToCheck = ArrayList<String>()
        val addOn = object : AnalyticsManagerImpl.AddOn {
            override fun logEvent(eventKey: String) {
                eventKeysToCheck.add(eventKey)
            }
        }
        val analyticsManager = createInstanceToTest(addOn)
        val eventKeys = listOf(
                "on_boarding_started",
                "on_boarding_try_dark_theme",
                "on_boarding_try_full_version",
                "on_boarding_subscription_clicked",
                "on_boarding_skip_clicked",
                "on_boarding_subscribed"
        )

        // When
        analyticsManager.sendEventOnBoardingStarted()
        analyticsManager.sendEventOnBoardingTryDarkTheme()
        analyticsManager.sendEventOnBoardingTryFullVersion()
        analyticsManager.sendEventOnBoardingSubscriptionClicked()
        analyticsManager.sendEventOnBoardingSkipClicked()
        analyticsManager.sendEventOnBoardingSubscribed()

        // Then
        Assert.assertEquals(eventKeys.size, eventKeysToCheck.size)
        for (i in 0 until eventKeys.size) {
            Assert.assertEquals(eventKeys[i], eventKeysToCheck[i])
        }
    }

    private fun createInstanceToTest(addOn: AnalyticsManagerImpl.AddOn): AnalyticsManager {
        return AnalyticsManagerImpl(
                addOn
        )
    }
}
