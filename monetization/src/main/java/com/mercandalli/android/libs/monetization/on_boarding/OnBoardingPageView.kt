package com.mercandalli.android.libs.monetization.on_boarding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import com.mercandalli.android.libs.monetization.R

class OnBoardingPageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        OnBoardingPageContract.Screen {

    private var card: View? = null

    fun initialize(@LayoutRes layout: Int) {
        val view = LayoutInflater.from(context).inflate(layout, this)
        card = view.findViewById(R.id.view_on_boarding_page_card)
    }

    fun applyAlphaToChildren(@FloatRange(from = 0.0, to = 1.0) securedAlpha: Float) {
        card?.let {
            it.scaleX = securedAlpha * 0.06F + 0.94F
            it.scaleY = securedAlpha * 0.15F + 0.85F
            it.alpha = securedAlpha * 0.02F + 0.98F
        }
    }
}