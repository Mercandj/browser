package com.mercandalli.android.browser.on_boarding

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.main.MainWebView

class OnBoardingPageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        OnBoardingPageContract.Screen {

    private var card: CardView? = null
    private var title: TextView? = null
    private var subtitle: TextView? = null
    private var gradientView: View? = null
    private var darkTheme: View? = null
    private var lightTheme: View? = null
    private var mainWebView: MainWebView? = null
    private var mainWebViewPlaceholder: View? = null
    private val userAction = createUserAction()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun reloadWebView() {
        mainWebView?.reload()
    }

    override fun showTryDarkTheme() {
        darkTheme?.visibility = VISIBLE
    }

    override fun hideTryDarkTheme() {
        darkTheme?.visibility = GONE
    }

    override fun showLightTheme() {
        lightTheme?.visibility = VISIBLE
    }

    override fun hideLightTheme() {
        lightTheme?.visibility = GONE
    }

    override fun setCardBackgroundColorRes(@ColorRes colorRes: Int) {
        card?.let {
            val color = ContextCompat.getColor(context, colorRes)
            it.setCardBackgroundColor(color)
        }
    }

    override fun setTextPrimaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        title?.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        subtitle?.setTextColor(color)
    }

    override fun showWebView() {
        mainWebView?.visibility = VISIBLE
    }

    override fun hideWebView() {
        mainWebView?.visibility = GONE
    }

    override fun showWebViewPlaceholder() {
        mainWebViewPlaceholder?.visibility = VISIBLE
    }

    override fun hideWebViewPlaceholder() {
        mainWebViewPlaceholder?.visibility = GONE
    }

    fun initialize(@LayoutRes layout: Int) {
        val view = LayoutInflater.from(context).inflate(layout, this)
        findViews(view)
        gradientView?.let {
            val animationDrawable = it.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(0)
            animationDrawable.setExitFadeDuration(4000)
            animationDrawable.start()
        }
        darkTheme?.setOnClickListener {
            userAction.onDarkThemeClicked()
        }
        lightTheme?.setOnClickListener {
            userAction.onLightThemeClicked()
        }
    }

    fun applyAlphaToChildren(@FloatRange(from = 0.0, to = 1.0) securedAlpha: Float) {
        card?.let {
            it.scaleX = securedAlpha * 0.06F + 0.94F
            it.scaleY = securedAlpha * 0.15F + 0.85F
            it.alpha = securedAlpha * 0.02F + 0.98F
        }
    }

    private fun findViews(view: View) {
        card = view.findViewById(R.id.view_on_boarding_page_card)
        title = view.findViewById(R.id.view_on_boarding_view_title)
        subtitle = view.findViewById(R.id.view_on_boarding_view_subtitle)
        mainWebView = view.findViewById(R.id.view_on_boarding_page_main_web_view)
        mainWebViewPlaceholder = view.findViewById(R.id.view_on_boarding_page_main_web_view_placeholder)
        gradientView = view.findViewById(R.id.view_on_boarding_page_gradient)
        darkTheme = view.findViewById(R.id.view_on_boarding_page_theme_dark)
        lightTheme = view.findViewById(R.id.view_on_boarding_page_theme_light)
    }

    private fun createUserAction(): OnBoardingPageContract.UserAction {
        if (isInEditMode) {
            return object : OnBoardingPageContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onDarkThemeClicked() {}
                override fun onLightThemeClicked() {}
            }
        }
        val themeManager = ApplicationGraph.getThemeManager()
        val networkManager = ApplicationGraph.getNetworkManager()
        return OnBoardingPagePresenter(
                this,
                themeManager,
                networkManager
        )
    }
}