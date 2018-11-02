package com.mercandalli.android.tv.browser.suggestion

import com.mercandalli.android.tv.browser.main.ApplicationGraph

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
