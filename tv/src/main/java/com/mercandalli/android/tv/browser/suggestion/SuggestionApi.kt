package com.mercandalli.android.tv.browser.suggestion

interface SuggestionApi {

    fun getSuggestionsJson(
        search: String
    ): String?

    fun getSuggestionsJson(
        search: String,
        block: (String) -> Unit
    )
}
