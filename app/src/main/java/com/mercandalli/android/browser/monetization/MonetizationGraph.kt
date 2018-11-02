package com.mercandalli.android.browser.monetization

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.browser.in_app.InAppManager
import com.mercandalli.android.browser.in_app.InAppModule
import com.mercandalli.android.browser.on_boarding.OnBoardingActivity
import com.mercandalli.android.browser.on_boarding.OnBoardingModule

class MonetizationGraph(
    private val context: Context,
    private val monetizationLog: MonetizationLog,
    private val activityAction: ActivityAction
) {

    private val inAppModule by lazy { InAppModule(context) }
    private val onBoardingModule by lazy { OnBoardingModule(context) }
    private val inAppManagerInternal by lazy { inAppModule.createInAppManager(monetizationLog) }
    private val monetizationManagerInternal by lazy { MonetizationManagerImpl() }
    private val onBoardingRepositoryInternal by lazy { onBoardingModule.createOnBoardingRepository() }

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
            monetizationLog: MonetizationLog,
            activityAction: ActivityAction
        ) {
            if (graph == null) {
                graph = MonetizationGraph(
                    context.applicationContext,
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
        fun isOnBoardingStorePageSkipped() = getOnBoardingRepository().isOnBoardingStorePageSkipped()

        @JvmStatic
        fun getInAppManager(): InAppManager = graph!!.inAppManagerInternal

        @JvmStatic
        internal fun getMonetizationManager(): MonetizationManager = graph!!.monetizationManagerInternal

        @JvmStatic
        internal fun getOnBoardingRepository() = graph!!.onBoardingRepositoryInternal

        @JvmStatic
        internal fun startFirstActivity() {
            graph!!.activityAction.startFirstActivity()
        }
    }
}
