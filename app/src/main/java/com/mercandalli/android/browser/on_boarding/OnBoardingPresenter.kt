package com.mercandalli.android.browser.on_boarding

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.mercandalli.android.browser.analytics.AnalyticsManager
import com.mercandalli.android.browser.floating.FloatingManager
import com.mercandalli.android.browser.monetization.MonetizationManager
import com.mercandalli.android.browser.in_app.InAppManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

internal class OnBoardingPresenter(
        private val screen: OnBoardingContract.Screen,
        private val analyticsManager: AnalyticsManager,
        private val floatingManager: FloatingManager,
        private val inAppManager: InAppManager,
        private val monetizationManager: MonetizationManager,
        private val onBoardingRepository: OnBoardingRepository,
        private val themeManager: ThemeManager,
        private val addOn: AddOn
) : OnBoardingContract.UserAction {

    private val monetizationEnabledListener = createMonetizationEnabledListener()
    private val inAppManagerListener = createInAppManagerListener()
    private val themeListener = createThemeListener()

    override fun onAttached() {
        monetizationManager.registerMonetizationListener(monetizationEnabledListener)
        syncScreen()
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetached() {
        monetizationManager.unregisterMonetizationListener(monetizationEnabledListener)
        inAppManager.unregisterListener(inAppManagerListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onPageChanged() {
        syncScreen()
    }

    override fun onNextClicked() {
        val lastPage = isLastPage()
        if (lastPage) {
            onBoardingRepository.markOnBoardingEnded()
            screen.closeOnBoarding()
            screen.startFistActivity()
            return
        }
        val page = screen.getPage()
        screen.setPage(page + 1)
    }

    override fun onStoreBuyClicked(activityContainer: InAppManager.ActivityContainer) {
        analyticsManager.sendEventOnBoardingSubscriptionClicked()
        inAppManager.registerListener(inAppManagerListener)
        val subscriptionFullVersionSku = addOn.getSubscriptionFullVersionSku()
        inAppManager.purchase(
                activityContainer,
                subscriptionFullVersionSku,
                BillingClient.SkuType.SUBS
        )
    }

    override fun onStoreSkipClicked() {
        analyticsManager.sendEventOnBoardingSkipClicked()
        onBoardingRepository.markOnBoardingStorePageSkipped()
        onBoardingRepository.markOnBoardingEnded()
        screen.closeOnBoarding()
        screen.startFistActivity()
        floatingManager.stop()
    }

    private fun syncScreen(
            onBoardingStorePageAvailable: Boolean = monetizationManager.isOnBoardingStorePageAvailable()
    ) {
        if (onBoardingStorePageAvailable) {
            screen.enableStorePage()
        } else {
            screen.disableStorePage()
        }
        val lastPage = isLastPage()
        if (lastPage && onBoardingStorePageAvailable) {
            inAppManager.initialize()
            screen.hideNextButton()
            screen.showStoreButtons()
        } else {
            screen.showNextButton()
            screen.hideStoreButtons()
        }
    }

    private fun isLastPage(): Boolean {
        val page = screen.getPage()
        val pageCount = screen.getPageCount()
        return page == pageCount - 1
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setPageIndicatorDarkTheme(themeManager.isDarkEnable())
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createMonetizationEnabledListener() = object : MonetizationManager.MonetizationListener {
        override fun onOnBoardingStorePageAvailableChanged() {
            syncScreen()
        }
    }

    private fun createInAppManagerListener() = object : InAppManager.Listener {
        override fun onSkuDetailsChanged(skuDetails: SkuDetails) {}
        override fun onPurchasedChanged() {
            val subscriptionFullVersionSku = addOn.getSubscriptionFullVersionSku()
            if (inAppManager.isPurchased(subscriptionFullVersionSku)) {
                analyticsManager.sendEventOnBoardingSubscribed()
                onBoardingRepository.markOnBoardingEnded()
                screen.closeOnBoarding()
                screen.startFistActivity()
            }
        }
    }

    interface AddOn {

        fun getSubscriptionFullVersionSku(): String
    }
}