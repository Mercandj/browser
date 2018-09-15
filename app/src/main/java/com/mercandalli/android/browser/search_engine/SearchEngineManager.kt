package com.mercandalli.android.browser.search_engine

interface SearchEngineManager {

    fun isFeatureAvailable(): Boolean

    fun getHomeUrl():String

    fun createSearchUrl(
            searchInput: String
    ): String

    @SearchEngine.Companion.SearchEngineKey
    fun getSearchEngineKey(): String

    fun getSearchEngine(): SearchEngine

    fun setSearchEngineKey(@SearchEngine.Companion.SearchEngineKey searchEngineKey: String)

    fun getSearchEngines(): List<SearchEngine>

    fun createSearchVideoUrl(
            searchInput: String
    ): String

    @SearchEngineVideo.Companion.SearchEngineVideoKey
    fun getSearchEngineVideoKey(): String

    fun getSearchEngineVideo(): SearchEngineVideo

    fun setSearchEngineVideoKey(@SearchEngineVideo.Companion.SearchEngineVideoKey searchEngineVideoKey: String)

    fun getSearchEngineVideos(): List<SearchEngineVideo>
}