@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.search_engine

import com.mercandalli.android.browser.main.ApplicationGraph

class SearchEngineModule {

    fun createSearchEngineManager(): SearchEngineManager {
        val productManager = ApplicationGraph.getProductManager()
        return SearchEngineManagerImpl(
            productManager
        )
    }
}
