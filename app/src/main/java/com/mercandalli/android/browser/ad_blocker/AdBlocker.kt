package com.mercandalli.android.browser.ad_blocker

import android.content.Context
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceResponse

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.util.HashSet

import androidx.annotation.WorkerThread

/**
 * https://github.com/AmniX/AdBlockedWebView-Android
 */
object AdBlocker {

    private const val AD_HOSTS_FILE = "hosts.txt"
    private val AD_HOSTS = HashSet<String>()

    fun init(context: Context) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                try {
                    loadFromAssets(context)
                } catch (e: IOException) {
                    // noop
                }

                return null
            }
        }.execute()
    }

    @WorkerThread
    private fun loadFromAssets(context: Context) {
        val stream = context.assets.open(AD_HOSTS_FILE)

        val inputStreamReader = InputStreamReader(stream)
        val readLines = inputStreamReader.readLines()
        AD_HOSTS.addAll(readLines)
    }

    fun isAd(url: String): Boolean {
        return try {
            isAdHost(UrlUtils.getHost(url))
        } catch (e: MalformedURLException) {
            Log.d("AmniX", e.toString())
            false
        }
    }

    fun createEmptyResource() = WebResourceResponse(
            "text/plain",
            "utf-8",
            ByteArrayInputStream("".toByteArray())
    )

    private fun isAdHost(host: String): Boolean {
        if (TextUtils.isEmpty(host)) {
            return false
        }
        val index = host.indexOf(".")
        val adHost = isAdHost(host.substring(index + 1))
        return index >= 0 && (AD_HOSTS.contains(host) || index + 1 < host.length && adHost)
    }
}