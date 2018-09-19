package com.mercandalli.android.browser.in_app

import android.content.Context
import com.mercandalli.android.browser.monetization.MonetizationLog

class InAppModule(
        private val context: Context
) {

    fun createInAppManager(monetizationLog: MonetizationLog): InAppManager {
        val playBillingManager = createPlayBillingManager()
        val inAppRepository = createInAppRepository()
        return InAppManagerImpl(
                playBillingManager,
                inAppRepository,
                monetizationLog
        )
    }

    private fun createPlayBillingManager() = PlayBillingManagerImpl(context.applicationContext)

    private fun createInAppRepository(): InAppRepository {
        val sharedPreferences = context.getSharedPreferences(
                InAppRepositoryImpl.PREFERENCE_NAME, Context.MODE_PRIVATE)
        return InAppRepositoryImpl(
                sharedPreferences
        )
    }
}