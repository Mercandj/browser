package com.mercandalli.android.tv.browser.suggestion

import com.mercandalli.android.tv.browser.main.ApplicationGraph
import com.mercandalli.android.wear.browser.suggestion.SuggestionManager
import com.mercandalli.android.wear.browser.suggestion.SuggestionManagerImpl

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