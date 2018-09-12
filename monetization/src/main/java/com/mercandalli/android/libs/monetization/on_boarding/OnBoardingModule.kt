package com.mercandalli.android.libs.monetization.on_boarding

import android.content.Context

class OnBoardingModule(
        private val context: Context
) {

    fun createOnBoardingRepository(): OnBoardingRepository {
        val sharedPreferences = context.getSharedPreferences(
                OnBoardingRepositoryImpl.PREFERENCE_NAME, Context.MODE_PRIVATE)
        return OnBoardingRepositoryImpl(
                sharedPreferences
        )
    }

}