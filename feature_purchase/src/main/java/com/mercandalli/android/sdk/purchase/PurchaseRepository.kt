@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.purchase

internal interface PurchaseRepository {

    fun addPurchased(sku: String): Boolean

    fun isPurchased(sku: String): Boolean

    fun isEmpty(): Boolean
}
