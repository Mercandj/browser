package com.mercandalli.android.libs.monetization.on_boarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercandalli.android.libs.monetization.R
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val onBoardingView = findViewById<OnBoardingView>(R.id.activity_on_boarding_view)
        onBoardingView.setCloseOnBoardingAction(object : OnBoardingView.CloseOnBoardingAction {
            override fun closeOnBoarding() {
                finish()
            }
        })
        onBoardingView.setCloseOnBoardingAction(object : InAppManager.ActivityContainer {
            override fun get() = this@OnBoardingActivity
        })
    }

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, OnBoardingActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}