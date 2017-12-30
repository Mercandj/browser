package com.mercandalli.android.browser.main

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * The [MainApplication] [dagger.Module]
 */
@Module
internal class MainModule(
        private val mainApplication: MainApplication) {

    @Provides
    @Singleton
    fun provideMainApplication(): MainApplication {
        return mainApplication
    }
}
