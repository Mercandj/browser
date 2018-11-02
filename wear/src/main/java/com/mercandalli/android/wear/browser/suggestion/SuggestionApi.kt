package com.mercandalli.android.wear.browser.suggestion

interface SuggestionApi {

    fun getSuggestionsJson(
        search: String
    ): String?

    fun getSuggestionsJson(
        search: String,
        block: (String) -> Unit
    )
}
