package com.mercandalli.android.wear.browser.suggestion

import com.mercandalli.android.wear.browser.main.ApplicationGraph

class SuggestionModule {

    fun createSuggestionManager(): SuggestionManager {
        val okHttpClientLazy = ApplicationGraph.getOkHttpClientLazy()
        val mainThreadPost = ApplicationGraph.getMainThreadPost()
        val suggestionApi = SuggestionApiImpl(okHttpClientLazy)
        return SuggestionManagerImpl(
                suggestionApi,
                mainThreadPost
        )
    }
}