package com.mercandalli.android.browser.theme

import android.content.Context
import com.mercandalli.android.browser.main.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ThemeModule {

    @Singleton
    @Provides
    fun provideThemeManager(
            mainApplication: MainApplication): ThemeManager {
        val sharedPreferences = mainApplication.getSharedPreferences(
                ThemeManagerImpl.PREFERENCE_NAME, Context.MODE_PRIVATE)

        return ThemeManagerImpl(sharedPreferences)
    }
}