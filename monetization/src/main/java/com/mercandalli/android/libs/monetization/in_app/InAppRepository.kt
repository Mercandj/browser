package com.mercandalli.android.libs.monetization.in_app

internal interface InAppRepository {

    fun addPurchased(sku: String)

    fun isPurchased(sku: String): Boolean
}