package com.mercandalli.android.browser.store

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.monetization.Monetization
import com.mercandalli.android.browser.monetization.MonetizationGraph
import com.mercandalli.android.browser.in_app.InAppManager

class StoreActivity : AppCompatActivity(),
        StoreContract.Screen {

    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        findViewById<View>(R.id.activity_store_buy_subscription).setOnClickListener {
            userAction.onBuySubscriptionClicked(object : InAppManager.ActivityContainer {
                override fun get() = this@StoreActivity
            })
        }
        val monetization = Monetization.fromJson(intent.extras.getString(EXTRA_MONETIZATION))
        userAction.onCreate(monetization)
    }

    private fun createUserAction(): StoreContract.UserAction {
        val inAppManager = MonetizationGraph.getInAppManager()
        return StorePresenter(
                this,
                inAppManager
        )
    }

    companion object {
        private const val EXTRA_MONETIZATION = "StoreActivity.Extra.EXTRA_MONETIZATION"

        @JvmStatic
        fun start(context: Context, monetization: Monetization) {
            val intent = Intent(context, StoreActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_MONETIZATION, Monetization.toJson(monetization))
            context.startActivity(intent)
        }
    }
}