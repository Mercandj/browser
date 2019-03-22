@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.on_boarding

import com.mercandalli.android.browser.analytics.AnalyticsManager
import com.mercandalli.android.browser.floating.FloatingManager
import com.mercandalli.android.browser.monetization.MonetizationManager
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.toast.ToastManager
import com.mercandalli.android.sdk.purchase.PurchaseDetails
import com.mercandalli.android.sdk.purchase.PurchaseManager

internal class OnBoardingViewPresenter(
    private val screen: OnBoardingViewContract.Screen,
    private val analyticsManager: AnalyticsManager,
    private val floatingManager: FloatingManager,
    private val monetizationManager: MonetizationManager,
    private val onBoardingRepository: OnBoardingRepository,
    private val purchaseManager: PurchaseManager,
    private val themeManager: ThemeManager,
    private val toastManager: ToastManager,
    private val addOn: AddOn
) : OnBoardingViewContract.UserAction {

    private val monetizationEnabledListener = createMonetizationEnabledListener()
    private val purchaseManagerListener = createPurchaseManagerListener()
    private val themeListener = createThemeListener()
    private var purchaseRequestTriggered = false

    override fun onAttached() {
        monetizationManager.registerMonetizationListener(monetizationEnabledListener)
        purchaseManager.registerListener(purchaseManagerListener)
        themeManager.registerThemeListener(themeListener)
        updateScreen()
    }

    override fun onDetached() {
        monetizationManager.unregisterMonetizationListener(monetizationEnabledListener)
        purchaseManager.unregisterListener(purchaseManagerListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onPageChanged() {
        updateScreen()
    }

    override fun onNextClicked() {
        val lastPage = isLastPage()
        if (lastPage) {
            onBoardingRepository.markOnBoardingEnded()
            screen.closeOnBoarding()
            screen.startFirstActivity()
            return
        }
        val page = screen.getPage()
        screen.setPage(page + 1)
    }

    override fun onStoreBuyClicked(
        activityContainer: PurchaseManager.ActivityContainer
    ) {
        purchaseRequestTriggered = true
        analyticsManager.sendEventOnBoardingSubscriptionClicked()
        purchaseManager.registerListener(purchaseManagerListener)
        val subscriptionFullVersionSku = addOn.getSubscriptionFullVersionSku()
        purchaseManager.purchase(
            activityContainer,
            subscriptionFullVersionSku,
            PurchaseManager.SUBS
        )
    }

    override fun onStoreSkipClicked() {
        analyticsManager.sendEventOnBoardingSkipClicked()
        onBoardingRepository.markOnBoardingStorePageSkipped()
        onBoardingRepository.markOnBoardingEnded()
        screen.closeOnBoarding()
        screen.startFirstActivity()
        floatingManager.stop()
    }

    override fun onSwipeOutAtEnd(activityContainer: PurchaseManager.ActivityContainer) {
        val onBoardingStorePageAvailable = monetizationManager.isOnBoardingStorePageAvailable()
        val lastPage = isLastPage()
        if (lastPage && onBoardingStorePageAvailable) {
            purchaseRequestTriggered = true
            analyticsManager.sendEventOnBoardingSubscriptionSwiped()
            val subscriptionFullVersionSku = addOn.getSubscriptionFullVersionSku()
            purchaseManager.purchase(
                activityContainer,
                subscriptionFullVersionSku,
                PurchaseManager.SUBS
            )
        }
    }

    private fun updateScreen(
        onBoardingStorePageAvailable: Boolean = monetizationManager.isOnBoardingStorePageAvailable()
    ) {
        if (onBoardingStorePageAvailable) {
            screen.enableStorePage()
        } else {
            screen.disableStorePage()
        }
        val lastPage = isLastPage()
        if (lastPage && onBoardingStorePageAvailable) {
            purchaseManager.initialize()
            screen.hideNextButton()
            screen.showStoreButtons()
        } else {
            screen.showNextButton()
            screen.hideStoreButtons()
        }
        updateTheme()
        updatePurchase()
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setPageIndicatorDarkTheme(themeManager.isDarkEnable())
    }

    private fun updatePurchase() {
        val subscriptionFullVersionSku = addOn.getSubscriptionFullVersionSku()
        val purchased = purchaseManager.isPurchased(subscriptionFullVersionSku)
        if (purchased) {
            if (purchaseRequestTriggered) {
                purchaseRequestTriggered = false
                analyticsManager.sendEventOnBoardingSubscribed()
            } else {
                toastManager.toast("Full version restored")
            }
            onBoardingRepository.markOnBoardingEnded()
            screen.closeOnBoarding()
            screen.startFirstActivity()
        }
    }

    private fun isLastPage(): Boolean {
        val page = screen.getPage()
        val pageCount = screen.getPageCount()
        return page == pageCount - 1
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createMonetizationEnabledListener() = object : MonetizationManager.MonetizationListener {
        override fun onOnBoardingStorePageAvailableChanged() {
            updateScreen()
        }
    }

    private fun createPurchaseManagerListener() = object : PurchaseManager.Listener {
        override fun onSkuDetailsChanged(purchaseDetails: PurchaseDetails) {}
        override fun onPurchasedChanged() {
            updatePurchase()
        }
    }

    interface AddOn {

        fun getSubscriptionFullVersionSku(): String
    }
}
