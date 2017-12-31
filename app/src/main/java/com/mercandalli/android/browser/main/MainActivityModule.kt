package com.mercandalli.android.browser.main

import dagger.Module
import dagger.Provides

@Module
internal class MainActivityModule(
        private val screen: MainActivityContract.Screen) {

    @Provides
    @MainActivityContract.Scope
    fun provideMainActivityScreen(): MainActivityContract.Screen {
        return this.screen
    }

    @Provides
    @MainActivityContract.Scope
    fun provideMainActivityUserAction(
            screen: MainActivityContract.Screen): MainActivityContract.UserAction {
        return MainActivityPresenter(screen)
    }
}
