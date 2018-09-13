package com.mercandalli.android.browser.search_engine

interface SearchEngineManager {

    fun isFeatureAvailable(): Boolean

    fun createSearchUrl(
            searchInput: String,
            @SearchEngine.Companion.SearchEngineKey searchEngineKey: String = getSearchEngineKey()
    ): String

    @SearchEngine.Companion.SearchEngineKey
    fun getSearchEngineKey(): String

    fun getSearchEngine(): SearchEngine

    fun setSearchEngineKey(@SearchEngine.Companion.SearchEngineKey searchEngineKey: String)

    fun getSearchEngines(): List<SearchEngine>
}