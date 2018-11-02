@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.in_app

import android.content.SharedPreferences
import org.json.JSONArray

internal class InAppRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : InAppRepository {

    private val purchasedSkus = HashSet<String>()
    private var initialized = false

    override fun addPurchased(sku: String) {
        initializeIfNeeded()
        purchasedSkus.add(sku)
        val jsonArray = JSONArray()
        for (purchasedSku in purchasedSkus) {
            jsonArray.put(purchasedSku)
        }
        sharedPreferences.edit().putString(KEY_PURCHASED_SKU, jsonArray.toString()).apply()
    }

    override fun isPurchased(sku: String): Boolean {
        initializeIfNeeded()
        return purchasedSkus.contains(sku)
    }

    private fun initializeIfNeeded() {
        if (initialized) {
            return
        }
        initialized = true
        purchasedSkus.clear()
        val json = sharedPreferences.getString(KEY_PURCHASED_SKU, null) ?: return
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val sku = jsonArray.getString(i)
            purchasedSkus.add(sku)
        }
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "in-app-repository"
        private const val KEY_PURCHASED_SKU = "in-app-repository.key-purchased-sku"
    }
}
