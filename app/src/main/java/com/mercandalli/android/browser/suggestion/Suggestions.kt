package com.mercandalli.android.browser.suggestion

import org.json.JSONArray

data class Suggestions(
        val searchInput: String,
        val suggestions: List<String>
) {

    companion object {

        fun createFromGoogle(jsonStr: String): Suggestions {
            val json = JSONArray(jsonStr)
            val suggestions = ArrayList<String>()
            val suggestionsJsonArray = json.getJSONArray(1)
            for (i in 0 until suggestionsJsonArray.length()) {
                suggestions.add(
                        suggestionsJsonArray.getJSONArray(i).getString(0)
                )
            }
            return Suggestions(
                    json.getString(0),
                    suggestions

            )
        }
    }
}