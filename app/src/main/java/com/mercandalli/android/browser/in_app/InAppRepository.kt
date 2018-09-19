package com.mercandalli.android.browser.in_app

internal interface InAppRepository {

    fun addPurchased(sku: String)

    fun isPurchased(sku: String): Boolean
}