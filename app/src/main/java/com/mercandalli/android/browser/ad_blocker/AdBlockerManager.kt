@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.ad_blocker

interface AdBlockerManager {

    fun isFeatureAvailable(): Boolean

    fun isEnabled(): Boolean

    fun setEnabled(enabled: Boolean)
}
