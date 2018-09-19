package com.mercandalli.android.browser.on_boarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.in_app.InAppManager
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.theme.Theme
import com.mercandalli.android.browser.theme.ThemeManager

class OnBoardingActivity : AppCompatActivity() {

    private val themeManager by lazy(LazyThreadSafetyMode.NONE) {
        ApplicationGraph.getThemeManager()
    }
    private val themeListener = createThemeListener()

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
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
        super.onDestroy()
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        setWindowBackgroundColorRes(theme.windowOnBoardingBackgroundColorRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarBackgroundColorRes(theme.windowOnBoardingBackgroundColorRes)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarDark(theme.statusBarDark)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNavigationBarColorRes(theme.windowBackgroundColorRes)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNavigationBarBarDark(theme.statusBarDark)
        }
    }

    private fun setWindowBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.setBackgroundDrawable(ColorDrawable(color))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setNavigationBarColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.navigationBarColor = color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = color
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        window.statusBarColor = color
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setStatusBarDark(statusBarDark: Boolean) {
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (statusBarDark)
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        else
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setNavigationBarBarDark(statusBarDark: Boolean) {
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (statusBarDark)
            flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        else
            flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, OnBoardingActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            context.startActivity(intent)
        }
    }
}