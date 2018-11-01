package com.mercandalli.android.browser.remote_config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.mercandalli.android.browser.BuildConfig
import com.mercandalli.android.browser.main_thread.MainThreadPost
import com.mercandalli.android.browser.update.UpdateManager

import java.util.ArrayList
import java.util.HashMap

import androidx.annotation.FloatRange

/**
 * This class is used to get the remote configuration from firebase
 */
internal class RemoteConfigImpl(
        updateManager: UpdateManager,
        private val mainThreadPost: MainThreadPost,
        @param:FloatRange(from = 0.0, to = 1.0) @field:FloatRange(from = 0.0, to = 1.0)
        private val randomFullVersionAvailablePercent: Float,
        @param:FloatRange(from = 0.0, to = 1.0) @field:FloatRange(from = 0.0, to = 1.0)
        private val randomOnBoardingStorePageAvailablePercent: Float
) : RemoteConfig {

    private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val listeners = ArrayList<RemoteConfig.Listener>()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        firebaseRemoteConfig.setConfigSettings(configSettings)
        firebaseRemoteConfig.setDefaults(defaultMap)
        val firstLaunchAfterUpdate = updateManager.isFirstLaunchAfterUpdate()
        val bypassCache = BuildConfig.DEBUG || firstLaunchAfterUpdate
        val task = if (bypassCache) firebaseRemoteConfig.fetch(0) else firebaseRemoteConfig.fetch()
        task.addOnCompleteListener { taskLocal ->
            if (taskLocal.isSuccessful) {
                firebaseRemoteConfig.activateFetched()
                notifyInitialized()
            }
        }
    }

    override fun isFullVersionAvailable() =
            randomFullVersionAvailablePercent <=
                    firebaseRemoteConfig.getDouble(FIREBASE_KEY_FULL_VERSION_AVAILABLE_PERCENT)

    override fun isOnBoardingStoreAvailable() =
            randomOnBoardingStorePageAvailablePercent <=
                    firebaseRemoteConfig.getDouble(FIREBASE_KEY_ON_BOARDING_STORE_PAGE_AVAILABLE_PERCENT)

    override fun getSubscriptionFullVersionSku() =
            firebaseRemoteConfig.getString(FIREBASE_KEY_SUBSCRIPTION_FULL_VERSION_SKU)
                    ?: DEFAULT_SUBSCRIPTION_FULL_VERSION_SKU

    override fun registerListener(listener: RemoteConfig.Listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    override fun unregisterListener(listener: RemoteConfig.Listener) {
        listeners.remove(listener)
    }

    private fun notifyInitialized() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { this.notifyInitialized() })
            return
        }
        for (i in listeners.indices) {
            listeners[i].onRemoteConfigChanged()
        }
    }

    companion object {

        private val defaultMap = HashMap<String, Any>()
        private const val FIREBASE_KEY_FULL_VERSION_AVAILABLE_PERCENT = "full_version_available_percent"
        private const val FIREBASE_KEY_ON_BOARDING_STORE_PAGE_AVAILABLE_PERCENT = "on_boarding_store_page_available_percent"
        private const val FIREBASE_KEY_SUBSCRIPTION_FULL_VERSION_SKU = "subscription_full_version_sku"

        private const val DEFAULT_SUBSCRIPTION_FULL_VERSION_SKU = "googleplay.com.mercandalli.android.browser.subscription.1"

        init {
            defaultMap[FIREBASE_KEY_FULL_VERSION_AVAILABLE_PERCENT] = 0
            defaultMap[FIREBASE_KEY_ON_BOARDING_STORE_PAGE_AVAILABLE_PERCENT] = 0
            defaultMap[FIREBASE_KEY_SUBSCRIPTION_FULL_VERSION_SKU] = DEFAULT_SUBSCRIPTION_FULL_VERSION_SKU
        }
    }
}
