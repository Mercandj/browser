package com.mercandalli.android.libs.monetization.on_boarding

import androidx.viewpager.widget.ViewPager

/**
 * A PageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 */
interface OnBoardingPageIndicator : ViewPager.OnPageChangeListener {

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view
     */
    fun setViewPager(view: ViewPager)

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view
     * @param initialPosition
     */
    fun setViewPager(view: ViewPager, initialPosition: Int)

    /**
     *
     * Set the current tutorial_page of both the ViewPager and indicator.
     *
     *
     * This **must** be used if you need to set the tutorial_page before
     * the views are drawn on screen (e.g., default start tutorial_page).
     *
     * @param item
     */
    fun setCurrentItem(item: Int)

    /**
     * Set a tutorial_page change listener which will receive forwarded events.
     *
     * @param listener
     */
    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener)

    /**
     * Notify the indicator that the fragment list has changed.
     */
    fun notifyDataSetChanged()
}