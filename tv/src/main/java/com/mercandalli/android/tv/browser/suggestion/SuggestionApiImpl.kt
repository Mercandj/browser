package com.mercandalli.android.tv.browser.suggestion

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.Callback
import okhttp3.Call
import java.io.Closeable
import java.io.IOException

class SuggestionApiImpl(
    private val okHttpClientLazy: Lazy<OkHttpClient>
) : SuggestionApi {

    override fun getSuggestionsJson(search: String): String? {
        return requestSync(
            createUrl(search),
            userAgent
        )
    }

    override fun getSuggestionsJson(search: String, block: (String) -> Unit) {
        val url = createUrl(search)
        val request = Request.Builder().url(url).header("User-Agent", userAgent).build()
        val call = okHttpClientLazy.value.newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                block.invoke(body!!.string())
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }

    private fun createUrl(search: String): String {
        val querySearch = search.replace(" ", "+")
        return "https://www.google.fr/complete/search?" +
            "client=psy-ab&" +
            "q=$querySearch"
    }

    private fun requestSync(url: String, userAgent: String): String? {
        val request = Request.Builder().url(url).header("User-Agent", userAgent).build()
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClientLazy.value.newCall(request).execute()
            body = response!!.body()
            return body!!.string()
        } catch (ignored: IOException) {
        } finally {
            closeSilently(body, response)
        }
        return null
    }

    private fun closeSilently(vararg xs: Closeable?) {
        for (x in xs) {
            if (x != null) {
                try {
                    x.close()
                } catch (ignored: Throwable) {
                }
            }
        }
    }

    companion object {
        const val userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13"
    }
}
