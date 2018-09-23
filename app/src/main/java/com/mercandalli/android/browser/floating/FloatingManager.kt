package com.mercandalli.android.browser.floating

interface FloatingManager {

    fun start(configuration: Configuration)

    fun stop()

    class Configuration private constructor(
            val url: String,
            val fullscreenButtonVisible: Boolean
    ) {

        companion object {

            fun default(url: String): Configuration {
                return Builder().build(url)
            }
        }

        class Builder {

            private var fullscreenButtonVisible: Boolean = true

            fun setFullscreenButtonVisible(visible: Boolean) {
                fullscreenButtonVisible = visible
            }

            fun build(url: String): Configuration {
                return Configuration(
                        url,
                        fullscreenButtonVisible
                )
            }
        }
    }
}