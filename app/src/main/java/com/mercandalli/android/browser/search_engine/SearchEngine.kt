package com.mercandalli.android.browser.search_engine

import androidx.annotation.StringDef

data class SearchEngine(
        @SearchEngineKey val searchEngineKey: String,
        val name: String,
        val homeUrl:String
) {

    companion object {

        @StringDef(
                SEARCH_ENGINE_GOOGLE,
                SEARCH_ENGINE_DUCK_DUCK_GO,
                SEARCH_ENGINE_BING,
                SEARCH_ENGINE_YAHOO,
                SEARCH_ENGINE_QWANT
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class SearchEngineKey

        const val SEARCH_ENGINE_GOOGLE = "search-engine-google"
        const val SEARCH_ENGINE_DUCK_DUCK_GO = "search-engine-duck-duck-go"
        const val SEARCH_ENGINE_BING = "search-engine-bing"
        const val SEARCH_ENGINE_YAHOO = "search-engine-yahoo"
        const val SEARCH_ENGINE_QWANT = "search-engine-qwant"
    }
}