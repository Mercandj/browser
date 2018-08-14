package com.mercandalli.android.browser.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.mercandalli.android.browser.R

class SettingsActivity : AppCompatActivity(),
        SettingsActivityContract.Screen {

    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<View>(R.id.activity_settings_back).setOnClickListener {
            userAction.onToolbarBackClicked()
        }
    }

    override fun quit() {
        finish()
    }

    private fun createUserAction(): SettingsActivityContract.UserAction {
        return SettingsActivityPresenter(
                this
        )
    }

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}