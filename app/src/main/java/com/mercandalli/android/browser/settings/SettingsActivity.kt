package com.mercandalli.android.browser.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.libs.monetization.in_app.InAppManager

class SettingsActivity : AppCompatActivity(),
        SettingsActivityContract.Screen {

    private val userAction = createUserAction()
    private val toolbar: View by bind(R.id.activity_settings_toolbar)
    private val back: ImageView by bind(R.id.activity_settings_back)
    private val title: TextView by bind(R.id.activity_settings_title)
    private val settingsView: SettingsView by bind(R.id.activity_settings_settings_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        back.setOnClickListener {
            userAction.onToolbarBackClicked()
        }
        settingsView.setActivityContainer(object : InAppManager.ActivityContainer {
            override fun get() = this@SettingsActivity
        })
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.statusBarColor = color
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