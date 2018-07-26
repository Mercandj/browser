package com.mercandalli.android.browser.thread

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainThreadModule {

    @Singleton
    @Provides
    fun provideMainThreadPost(): MainThreadPost {
        val mainLooper = Looper.getMainLooper()
        return MainThreadPostImpl(
                mainLooper.thread,
                Handler(mainLooper)
        )
    }
}
