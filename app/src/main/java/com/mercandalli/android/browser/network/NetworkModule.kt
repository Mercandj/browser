package com.mercandalli.android.browser.network

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NetworkModule(
    private val context: Context
) {

    private val okHttpClient = lazy {
        val builder = OkHttpClient.Builder()
        // if (BuildConfig.DEBUG) {
        //     HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //     interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //     builder.addInterceptor(interceptor);
        // }
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.build()
    }

    fun createOkHttpClientLazy(): Lazy<OkHttpClient> = okHttpClient

    fun createNetworkManager(): NetworkManager = NetworkManagerImpl(context)
}
