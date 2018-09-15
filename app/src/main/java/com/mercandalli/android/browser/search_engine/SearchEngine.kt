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
                SEARCH_ENGINE_DUCK_DUCK_GO
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class SearchEngineKey

        const val SEARCH_ENGINE_GOOGLE = "search-engine-google"
        const val SEARCH_ENGINE_DUCK_DUCK_GO = "search-engine-duck-duck-go"
    }
}