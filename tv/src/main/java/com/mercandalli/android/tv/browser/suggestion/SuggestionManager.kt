package com.mercandalli.android.wear.browser.suggestion

interface SuggestionManager {

    fun getSuggestion(
            searchInput: String
    )

    fun registerSuggestionListener(listener: SuggestionListener)

    fun unregisterSuggestionListener(listener: SuggestionListener)

    interface SuggestionListener {

        fun onSuggestionEnded(suggestions: Suggestions)
    }
}