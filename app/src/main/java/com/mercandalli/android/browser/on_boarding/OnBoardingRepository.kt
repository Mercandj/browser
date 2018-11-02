@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.on_boarding

interface OnBoardingRepository {

    fun isOnBoardingEnded(): Boolean

    fun markOnBoardingEnded()

    fun isOnBoardingStorePageSkipped(): Boolean

    fun markOnBoardingStorePageSkipped()

    fun clear()
}
