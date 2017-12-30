package com.mercandalli.android.browser.main

import com.mercandalli.android.browser.thread.MainThreadModule
import com.mercandalli.android.browser.thread.MainThreadPost
import dagger.Component
import javax.inject.Singleton

/**
 * The [MainApplication] [Component].
 */
@Singleton
@Component(modules = arrayOf(
        MainModule::class,
        MainThreadModule::class))
interface MainComponent {

    fun provideMainThreadPost(): MainThreadPost

    fun provideMainApplication(): MainApplication
}
