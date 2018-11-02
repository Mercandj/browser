package com.mercandalli.android.browser.suggestion

import com.mercandalli.android.browser.main_thread.MainThreadPost

internal class SuggestionManagerImpl(
    private val suggestionApi: SuggestionApi,
    private val mainThreadPost: MainThreadPost
) : SuggestionManager {

    private val listeners = ArrayList<SuggestionManager.SuggestionListener>()

    override fun getSuggestion(searchInput: String) {
        suggestionApi.getSuggestionsJson(searchInput) { json ->
            run {
                val suggestions = Suggestions.createFromGoogle(searchInput, json)
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
