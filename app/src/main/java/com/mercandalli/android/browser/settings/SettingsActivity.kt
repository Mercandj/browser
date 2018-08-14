package com.mercandalli.android.browser.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph

class SettingsActivity : AppCompatActivity(),
        SettingsActivityContract.Screen {

    private val userAction = createUserAction()
    private val toolbar: View by bind(R.id.activity_settings_toolbar)
    private val back: ImageView by bind(R.id.activity_settings_back)
    private val title: TextView by bind(R.id.activity_settings_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        back.setOnClickListener {
            userAction.onToolbarBackClicked()
        }
        userAction.onCreate()
    }

    override fun onDestroy() {
        userAction.onDestroy()
        super.onDestroy()
    }

    override fun quit() {
        finish()
    }

    override fun setWindowBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun setTextDarkColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        back.setColorFilter(color)
        title.setTextColor(color)
    }

    override fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        toolbar.setBackgroundColor(color)
    }

    private fun createUserAction(): SettingsActivityContract.UserAction {
        val themeManager = ApplicationGraph.getThemeManager()
        return SettingsActivityPresenter(
                this,
                themeManager
        )
    }

    private fun <T : View> bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
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