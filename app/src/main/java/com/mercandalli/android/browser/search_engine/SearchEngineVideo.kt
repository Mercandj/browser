package com.mercandalli.android.browser.search_engine

import androidx.annotation.StringDef

data class SearchEngineVideo(
        @SearchEngineVideo.Companion.SearchEngineVideoKey val searchEngineVideoKey: String,
        val name: String
) {

    companion object {

        @StringDef(
                SEARCH_ENGINE_VIDEO_YOUTUBE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class SearchEngineVideoKey

        const val SEARCH_ENGINE_VIDEO_YOUTUBE = "search-engine-youtube"
    }
}