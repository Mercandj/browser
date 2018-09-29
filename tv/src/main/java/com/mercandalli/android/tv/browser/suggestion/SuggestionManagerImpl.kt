package com.mercandalli.android.wear.browser.suggestion

import com.mercandalli.android.tv.browser.main_thread.MainThreadPost
import com.mercandalli.android.tv.browser.suggestion.SuggestionApi

internal class SuggestionManagerImpl(
        private val suggestionApi: SuggestionApi,
        private val mainThreadPost: MainThreadPost
) : SuggestionManager {

    private val listeners = ArrayList<SuggestionManager.SuggestionListener>()

    override fun getSuggestion(searchInput: String) {
        suggestionApi.getSuggestionsJson(searchInput) { json ->
            run {
                val suggestions = Suggestions.createFromGoogle(json)
                notify(suggestions)
            }
        }
    }

    override fun registerSuggestionListener(listener: SuggestionManager.SuggestionListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterSuggestionListener(listener: SuggestionManager.SuggestionListener) {
        listeners.remove(listener)
    }

    private fun notify(suggestions: Suggestions) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                notify(suggestions)
            })
            return
        }
        for (listener in listeners) {
            listener.onSuggestionEnded(suggestions)
        }
    }
}