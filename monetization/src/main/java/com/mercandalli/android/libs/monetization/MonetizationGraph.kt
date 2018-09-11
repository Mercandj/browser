package com.mercandalli.android.libs.monetization

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.libs.monetization.in_app.*
import com.mercandalli.android.libs.monetization.log.MonetizationLog

class MonetizationGraph(
        private val context: Context,
        private val monetizationLog: MonetizationLog
) {

    private val inAppModule by lazy {
        InAppModule(context)
    }

    private val inAppManagerInternal by lazy {
        inAppModule.createInAppManager(monetizationLog)
    }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: MonetizationGraph? = null

        @JvmStatic
        fun init(
                context: Context,
                monetizationLog: MonetizationLog
        ) {
            if (graph == null) {
                graph = MonetizationGraph(
                        context.applicationContext,
                        monetizationLog
                )
            }
        }

        @JvmStatic
        fun getInAppManager(): InAppManager = graph!!.inAppManagerInternal
    }
}