package com.mercandalli.android.libs.monetization.on_boarding

import android.content.SharedPreferences

class OnBoardingRepositoryImpl(
        private val sharedPreferences: SharedPreferences
) : OnBoardingRepository {

    private var onBoardingEnded = false
    private var onBoardingEndedLoaded = false
    private var onBoardingStoreSkipped = false
    private var onBoardingStoreSkippedLoaded = false

    override fun isOnBoardingEnded(): Boolean {
        loadSeen()
        return onBoardingEnded
    }

    override fun markOnBoardingEnded() {
        loadSeen()
        onBoardingEnded = true
        sharedPreferences.edit().putBoolean(KEY_ON_BOARDING_ENDED, onBoardingEnded).apply()
    }

    override fun isOnBoardingStorePageSkipped(): Boolean {
        loadSkipped()
        return onBoardingStoreSkipped
    }

    override fun markOnBoardingStorePageSkipped() {
        loadSkipped()
        onBoardingStoreSkipped = true
        sharedPreferences.edit().putBoolean(KEY_ON_BOARDING_STORE_SKIPPED, onBoardingStoreSkipped).apply()
    }

    private fun loadSeen() {
        if (onBoardingEndedLoaded) {
            return
        }
        onBoardingEndedLoaded = true
        onBoardingEnded = sharedPreferences.getBoolean(KEY_ON_BOARDING_ENDED, onBoardingEnded)
    }

    private fun loadSkipped() {
        if (onBoardingStoreSkippedLoaded) {
            return
        }
        onBoardingStoreSkippedLoaded = true
        onBoardingStoreSkipped = sharedPreferences.getBoolean(KEY_ON_BOARDING_STORE_SKIPPED, onBoardingStoreSkipped)
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "on-boarding"
        private const val KEY_ON_BOARDING_ENDED = "on-boarding-ended"
        private const val KEY_ON_BOARDING_STORE_SKIPPED = "on-boarding-store-skipped"
    }
}