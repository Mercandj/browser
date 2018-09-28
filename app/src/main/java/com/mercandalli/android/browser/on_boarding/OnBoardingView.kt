package com.mercandalli.android.browser.on_boarding

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.monetization.MonetizationGraph
import com.mercandalli.android.browser.in_app.InAppManager
import com.mercandalli.android.browser.main.ApplicationGraph

class OnBoardingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        OnBoardingContract.Screen {

    private val layoutNamesWithoutStore = listOf(
            R.layout.view_on_boading_page_1,
            R.layout.view_on_boading_page_2
    )
    private val layoutNamesWithStore = listOf(
            R.layout.view_on_boading_page_1,
            R.layout.view_on_boading_page_2,
            R.layout.view_on_boading_page_3
    )
    private var currentLayoutNames = layoutNamesWithoutStore
    private val view = LayoutInflater.from(context).inflate(R.layout.view_on_boarding, this)
    private val viewPager: ViewPager = view.findViewById(R.id.view_on_boarding_view_pager)
    private val storeBuy: TextView = view.findViewById(R.id.view_on_boarding_store_buy)
    private val storeSkip: TextView = view.findViewById(R.id.view_on_boarding_store_skip)
    private val next: TextView = view.findViewById(R.id.view_on_boarding_next)
    private val title: TextView = view.findViewById(R.id.view_on_boarding_title)
    private val indicatorOnBoarding: OnBoardingPageIndicator = view.findViewById<OnBoardingPageIndicatorView>(R.id.view_on_boarding_indicator)
    private val onPageChangeListener = createOnPageChangeListener()
    private val pages = SparseArray<OnBoardingPageView>()
    private val adapter = createPagerAdapter()
    private val userAction = createUserAction()

    private var closeOnBoardingAction: CloseOnBoardingAction? = null
    private var activityContainer: InAppManager.ActivityContainer? = null

    init {
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(onPageChangeListener)
        next.setOnClickListener {
            userAction.onNextClicked()
        }
        storeBuy.setOnClickListener {
            userAction.onStoreBuyClicked(activityContainer!!)
        }
        storeSkip.setOnClickListener {
            userAction.onStoreSkipClicked()
        }
        indicatorOnBoarding.setViewPager(viewPager)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    @IntRange(from = 0)
    override fun getPage() = viewPager.currentItem

    override fun setPage(@IntRange(from = 0) page: Int) {
        viewPager.currentItem = page
    }

    @IntRange(from = 0)
    override fun getPageCount() = currentLayoutNames.size

    override fun enableStorePage() {
        currentLayoutNames = layoutNamesWithStore
        adapter.notifyDataSetChanged()
    }

    override fun disableStorePage() {
        currentLayoutNames = layoutNamesWithoutStore
        adapter.notifyDataSetChanged()
    }

    override fun showNextButton() {
        next.visibility = VISIBLE
    }

    override fun hideNextButton() {
        next.visibility = GONE
    }

    override fun showStoreButtons() {
        storeBuy.visibility = VISIBLE
        storeSkip.visibility = VISIBLE
    }

    override fun hideStoreButtons() {
        storeBuy.visibility = GONE
        storeSkip.visibility = GONE
    }

    override fun closeOnBoarding() {
        closeOnBoardingAction!!.closeOnBoarding()
    }

    override fun startFistActivity() {
        MonetizationGraph.startFirstActivity()
    }

    override fun setTextPrimaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        title.setTextColor(color)
    }

    override fun setTextSecondaryColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        storeSkip.setTextColor(color)
    }

    override fun setPageIndicatorDarkTheme(darkEnabled: Boolean) {
        indicatorOnBoarding.setDarkTheme(darkEnabled)
    }

    fun setCloseOnBoardingAction(action: CloseOnBoardingAction) {
        closeOnBoardingAction = action
    }

    fun setCloseOnBoardingAction(activityContainer: InAppManager.ActivityContainer) {
        this.activityContainer = activityContainer
    }

    private fun createPagerAdapter() = object : PagerAdapter() {

        override fun getCount() = getPageCount()

        override fun isViewFromObject(@NonNull view: View, @NonNull item: Any): Boolean {
            return view == item
        }

        @NonNull
        override fun instantiateItem(@NonNull container: ViewGroup, @IntRange(from = 0) position: Int): Any {
            val layoutResForPage = currentLayoutNames[position]
            val onBoardingPageView = OnBoardingPageView(container.context)
            onBoardingPageView.initialize(layoutResForPage)
            container.addView(onBoardingPageView)
            pages.put(position, onBoardingPageView)
            return onBoardingPageView
        }

        override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull item: Any) {
            val onBoardingPageView = item as OnBoardingPageView
            container.removeView(onBoardingPageView)
        }
    }

    private fun createOnPageChangeListener() = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val onBoardingPageViewLeft = pages.get(position)
            val onBoardingPageViewRight = pages.get(position + 1)
            if (onBoardingPageViewLeft != null) {
                @FloatRange(from = -1.0, to = 1.0)
                val alpha = (1 - positionOffset) * 2 - 1
                @FloatRange(from = 0.0, to = 1.0)
                val securedAlpha = Math.max(alpha, 0f)
                onBoardingPageViewLeft.applyAlphaToChildren(securedAlpha)
            }
            if (onBoardingPageViewRight != null) {
                @FloatRange(from = -1.0, to = 1.0)
                val alpha = positionOffset * 2 - 1f
                @FloatRange(from = 0.0, to = 1.0)
                val securedAlpha = Math.max(alpha, 0f)
                onBoardingPageViewRight.applyAlphaToChildren(securedAlpha)
            }
        }

        override fun onPageSelected(position: Int) {
            userAction.onPageChanged()
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    private fun createUserAction() = if (isInEditMode) {
        object : OnBoardingContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onPageChanged() {}
            override fun onNextClicked() {}
            override fun onStoreBuyClicked(activityContainer: InAppManager.ActivityContainer) {}
            override fun onStoreSkipClicked() {}
        }
    } else {
        val analyticsManager = ApplicationGraph.getAnalyticsManager()
        val floatingManager = ApplicationGraph.getFloatingManager()
        val inAppManager = MonetizationGraph.getInAppManager()
        val monetizationManager = MonetizationGraph.getMonetizationManager()
        val onBoardingRepository = MonetizationGraph.getOnBoardingRepository()
        val subscriptionSku = MonetizationGraph.getMonetization().subscriptionSku
        val themeManager = ApplicationGraph.getThemeManager()
        OnBoardingPresenter(
                this,
                analyticsManager,
                floatingManager,
                inAppManager,
                monetizationManager,
                onBoardingRepository,
                subscriptionSku,
                themeManager
        )
    }

    interface CloseOnBoardingAction {
        fun closeOnBoarding()
    }
}