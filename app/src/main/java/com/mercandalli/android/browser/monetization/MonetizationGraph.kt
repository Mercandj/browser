package com.mercandalli.android.browser.monetization

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.browser.in_app.InAppManager
import com.mercandalli.android.browser.in_app.InAppModule
import com.mercandalli.android.browser.on_boarding.OnBoardingActivity
import com.mercandalli.android.browser.on_boarding.OnBoardingModule
import com.mercandalli.android.browser.store.StoreActivity

class MonetizationGraph(
        private val context: Context,
        private val monetization: Monetization,
        private val monetizationLog: MonetizationLog,
        private val activityAction: ActivityAction
) {

    private val inAppModule by lazy {
        InAppModule(context)
    }

    private val inAppManagerInternal by lazy {
        inAppModule.createInAppManager(monetizationLog)
    }

    private val monetizationManagerInternal by lazy {
        MonetizationManagerImpl()
    }

    private val onBoardingRepositoryInternal by lazy {
        OnBoardingModule(context).createOnBoardingRepository()
    }

    interface ActivityAction {
        fun startFirstActivity()
    }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: MonetizationGraph? = null

        @JvmStatic
        fun init(
                context: Context,
                monetization: Monetization,
                monetizationLog: MonetizationLog,
                activityAction: ActivityAction
        ) {
            if (graph == null) {
                graph = MonetizationGraph(
                        context.applicationContext,
                        monetization,
                        monetizationLog,
                        activityAction
                )
            }
        }

        @JvmStatic
        fun setOnBoardingStorePageAvailable(available: Boolean) {
            val monetizationManager = getMonetizationManager()
            monetizationManager.setOnBoardingStorePageAvailable(available)
        }

        @JvmStatic
        fun startOnBoardingIfNeeded(context: Context): Boolean {
            val onBoardingRepository = getOnBoardingRepository()
            if (onBoardingRepository.isOnBoardingEnded()) {
                return false
            }
            OnBoardingActivity.start(context)
            return true
        }

        @JvmStatic
        fun startStore(context: Context) {
            val monetization = graph!!.monetization
            StoreActivity.start(context, monetization)
        }

        @JvmStatic
        fun isOnBoardingStorePageSkipped() = getOnBoardingRepository().isOnBoardingStorePageSkipped()

        @JvmStatic
        fun getInAppManager(): InAppManager = graph!!.inAppManagerInternal

        @JvmStatic
        internal fun getMonetizationManager(): MonetizationManager = graph!!.monetizationManagerInternal

        @JvmStatic
        internal fun getMonetization(): Monetization = graph!!.monetization

        @JvmStatic
        internal fun getOnBoardingRepository() = graph!!.onBoardingRepositoryInternal

        @JvmStatic
        internal fun startFirstActivity() {
            graph!!.activityAction.startFirstActivity()
        }
    }
}