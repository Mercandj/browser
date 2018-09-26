package com.mercandalli.android.browser.on_boarding

interface OnBoardingRepository {

    fun isOnBoardingEnded(): Boolean

    fun markOnBoardingEnded()

    fun isOnBoardingStorePageSkipped(): Boolean

    fun markOnBoardingStorePageSkipped()

    fun clear()
}