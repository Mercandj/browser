@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.search_engine

import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.search_engine.SearchEngine.Companion.SEARCH_ENGINE_BING
import com.mercandalli.android.browser.search_engine.SearchEngine.Companion.SEARCH_ENGINE_DUCK_DUCK_GO
import com.mercandalli.android.browser.search_engine.SearchEngine.Companion.SEARCH_ENGINE_GOOGLE
import com.mercandalli.android.browser.search_engine.SearchEngine.Companion.SEARCH_ENGINE_QWANT
import com.mercandalli.android.browser.search_engine.SearchEngine.Companion.SEARCH_ENGINE_YAHOO
import com.mercandalli.android.browser.search_engine.SearchEngineVideo.Companion.SEARCH_ENGINE_VIDEO_YOUTUBE

class SearchEngineManagerImpl(
    private val productManager: ProductManager
) : SearchEngineManager {

    @SearchEngine.Companion.SearchEngineKey
    private var searchEngineKey = SEARCH_ENGINE_GOOGLE

    @SearchEngineVideo.Companion.SearchEngineVideoKey
    private var searchEngineVideoKey = SEARCH_ENGINE_VIDEO_YOUTUBE

    private val searchEngines = ArrayList<SearchEngine>()
    private val searchEngineVideos = ArrayList<SearchEngineVideo>()

    init {
        searchEngines.add(SearchEngine(SEARCH_ENGINE_GOOGLE, "Google", "https://www.google.com/"))
        searchEngines.add(SearchEngine(SEARCH_ENGINE_DUCK_DUCK_GO, "DuckDuckGo", "https://www.google.com/"))
        searchEngines.add(SearchEngine(SEARCH_ENGINE_BING, "Bing", "https://www.bing.com/"))
        searchEngines.add(SearchEngine(SEARCH_ENGINE_YAHOO, "Yahoo", "https://yahoo.com/"))
        searchEngines.add(SearchEngine(SEARCH_ENGINE_QWANT, "Qwant", "https://www.qwant.com/"))
        searchEngineVideos.add(SearchEngineVideo(SEARCH_ENGINE_VIDEO_YOUTUBE, "YouTube"))
    }

    override fun getHomeUrl() = getSearchEngine().homeUrl

    override fun isFeatureAvailable() = productManager.isFullVersionAvailable()

    override fun createSearchUrl(
        searchInput: String
    ): String {
        if (searchInput.startsWith("https://") || searchInput.startsWith("http://")) {
            return searchInput
        }
        return when (searchEngineKey) {
            SEARCH_ENGINE_GOOGLE -> searchGoogle(searchInput)
            SEARCH_ENGINE_DUCK_DUCK_GO -> searchDuckDuckGo(searchInput)
            SEARCH_ENGINE_BING -> searchBing(searchInput)
            SEARCH_ENGINE_YAHOO -> searchYahoo(searchInput)
            SEARCH_ENGINE_QWANT -> searchQwant(searchInput)
            else -> searchGoogle(searchInput)
        }
    }

    @SearchEngine.Companion.SearchEngineKey
    override fun getSearchEngineKey(): String {
        return searchEngineKey
    }

    override fun getSearchEngine(): SearchEngine {
        for (searchEngine in searchEngines) {
            if (searchEngineKey == searchEngine.searchEngineKey) {
                return searchEngine
            }
        }
        throw IllegalStateException("searchEngineKey: $searchEngineKey not found")
    }

    override fun setSearchEngineKey(searchEngineKey: String) {
        this.searchEngineKey = searchEngineKey
    }

    override fun getSearchEngines() = ArrayList<SearchEngine>(searchEngines)

    override fun createSearchVideoUrl(searchInput: String): String {
        if (searchInput.startsWith("https://") || searchInput.startsWith("http://")) {
            return searchInput
        }
        return when (searchEngineVideoKey) {
            SEARCH_ENGINE_VIDEO_YOUTUBE -> searchYouTube(searchInput)
            else -> searchGoogle(searchInput)
        }
    }

    override fun getSearchEngineVideoKey() = searchEngineVideoKey

    override fun getSearchEngineVideo(): SearchEngineVideo {
        for (searchEngine in searchEngineVideos) {
            if (searchEngineKey == searchEngine.searchEngineVideoKey) {
                return searchEngine
            }
        }
        throw IllegalStateException("searchEngineVideoKey: $searchEngineVideoKey not found")
    }

    override fun setSearchEngineVideoKey(searchEngineVideoKey: String) {
        this.searchEngineVideoKey = searchEngineVideoKey
    }

    override fun getSearchEngineVideos() = ArrayList<SearchEngineVideo>(searchEngineVideos)

    private fun searchYouTube(searchInput: String) =
        "https://www.youtube.com/results?utm_source=opensearch&search_query=" + searchInput.replace(" ", "+")

    private fun searchGoogle(searchInput: String) =
        "https://www.google.fr/search?q=" + searchInput.replace(" ", "+")

    private fun searchDuckDuckGo(searchInput: String) =
        "https://duckduckgo.com/?q=" + searchInput.replace(" ", "+")

    private fun searchBing(searchInput: String) =
        "https://www.bing.com/search?q=" + searchInput.replace(" ", "+")

    private fun searchYahoo(searchInput: String) =
        "https://search.yahoo.com/search?p=" + searchInput.replace(" ", "+")

    private fun searchQwant(searchInput: String) =
        "https://www.qwant.com/?q=" + searchInput.replace(" ", "+")
}
