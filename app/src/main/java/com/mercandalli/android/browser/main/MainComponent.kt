package com.mercandalli.android.browser.main

import com.mercandalli.android.browser.theme.ThemeManager
import com.mercandalli.android.browser.theme.ThemeModule
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
        MainThreadModule::class,
        ThemeModule::class))
interface MainComponent {

    fun provideMainThreadPost(): MainThreadPost

    fun provideMainApplication(): MainApplication

    fun provideThemeManager(): ThemeManager
}
