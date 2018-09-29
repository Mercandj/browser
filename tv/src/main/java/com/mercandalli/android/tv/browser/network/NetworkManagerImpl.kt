package com.mercandalli.android.tv.browser.network

import android.content.Context
import android.net.ConnectivityManager
import com.mercandalli.android.tv.browser.network.NetworkManager

class NetworkManagerImpl(
        private val context: Context
) : NetworkManager {

    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun isNetworkAvailable() = connectivityManager.activeNetworkInfo != null &&
            connectivityManager.activeNetworkInfo.isConnected
}