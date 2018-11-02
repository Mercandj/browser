@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.in_app

internal interface InAppRepository {

    fun addPurchased(sku: String)

    fun isPurchased(sku: String): Boolean
}
