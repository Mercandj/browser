package com.mercandalli.android.browser.hash

interface HashManager {


    fun sha256(text: String?, time: Int): String?
}