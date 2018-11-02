package com.mercandalli.android.browser.screenshot

import tools.fastlane.screengrab.Screengrab

class ScreenShooter(
    private val prefixFileName: String
) {
    private var index = 1

    fun shoot(name: String) {
        Screengrab.screenshot("${createIndex()}_${name}_$prefixFileName")
        index++
    }

    private fun createIndex() = if (index < 10) "0$index" else index.toString()
}
